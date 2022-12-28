package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.model.Room;
import pl.edu.agh.cs.to.cinemamak.model.Seat;
import pl.edu.agh.cs.to.cinemamak.model.Ticket;
import pl.edu.agh.cs.to.cinemamak.service.PerformanceService;
import pl.edu.agh.cs.to.cinemamak.service.RoomService;
import pl.edu.agh.cs.to.cinemamak.service.TicketService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@FxmlView("tickets-view.fxml")
public class TicketsController {
    @FXML
    private TableView<Performance> performancesList;

    @FXML
    private TableColumn<Performance, String> titleColumn;

    @FXML
    private TableColumn<Performance, String> dateColumn;

    @FXML
    private TableColumn<Performance, BigDecimal> priceColumn;

    @FXML
    private TableColumn<Performance, String> roomColumn;

    @FXML
    private GridPane seatsTable;

    @FXML
    private Button sellButton;

    @FXML
    private Button cancelReservationButton;

    private final PerformanceService performanceService;
    private final RoomService roomService;
    private final TicketService ticketService;

    private final ObjectProperty<Optional<Seat>> selectedSeat = new SimpleObjectProperty<>(Optional.empty());

    public TicketsController(PerformanceService performanceService, RoomService roomService, TicketService ticketService) {
        this.performanceService = performanceService;
        this.roomService = roomService;
        this.ticketService = ticketService;
    }

    public void initialize() {
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMovie().getTitle()));

        dateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDate()
                .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))));

        priceColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPrice()));

        roomColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getRoom().getName()));

        performancesList.setItems(getPerformances());

        performancesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setSeatValues(newValue.getRoom()));

        sellButton.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedSeat.getValue().isEmpty()
                || ticketService.isSeatTaken(getSelectedPerformance(), selectedSeat.getValue().get()),
                selectedSeat,
                performancesList.getSelectionModel().selectedItemProperty()));

        cancelReservationButton.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedSeat.getValue().isEmpty()
                        || !ticketService.isSeatTaken(getSelectedPerformance(), selectedSeat.getValue().get()),
                selectedSeat,
                performancesList.getSelectionModel().selectedItemProperty()));
    }

    private ObservableList<Performance> getPerformances() {
        ObservableList<Performance> res = FXCollections.observableArrayList();
        performanceService.getPerformances().ifPresent(res::addAll);
        return res;
    }

    private Performance getSelectedPerformance() {
        return performancesList.getSelectionModel().getSelectedItem();
    }

    private void setSeatValues(Room room) {
        selectedSeat.setValue(Optional.empty());
        seatsTable.getChildren().removeAll(seatsTable.getChildren());
        roomService.getRoomSeats(room).forEach(seat -> {
            var b = new Button();
            b.setText(seat.getName());

            if (ticketService.isSeatTaken(getSelectedPerformance(), seat)) {
                b.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }

            b.setOnAction(e -> selectedSeat.setValue(Optional.of(seat)));

            b.backgroundProperty().bind(Bindings.createObjectBinding(() -> {
                if (selectedSeat.getValue().isPresent() && selectedSeat.getValue().get() == seat) {
                    return new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY));
                } else {
                    return new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY));
                }
            }, selectedSeat));

            seatsTable.add(b, (int)seat.getColNumber(), (int)seat.getRowNumber());
        });
    }

    @FXML
    private void sellTicket() {
        if (selectedSeat.getValue().isPresent()) {
            var t = new Ticket(getSelectedPerformance(), selectedSeat.getValue().get());
            ticketService.addSoldTicket(t);
            setSeatValues(getSelectedPerformance().getRoom());
        }
    }

    @FXML
    private void cancelReservation() {
        if (selectedSeat.getValue().isPresent()) {
            var t = ticketService.getTicket(getSelectedPerformance(), selectedSeat.getValue().get());
            t.ifPresent(ticketService::removeTicket);
            setSeatValues(getSelectedPerformance().getRoom());
        }
    }
}
