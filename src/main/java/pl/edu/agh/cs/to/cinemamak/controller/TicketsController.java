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
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.*;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;
import pl.edu.agh.cs.to.cinemamak.service.PerformanceService;
import pl.edu.agh.cs.to.cinemamak.service.RoomService;
import pl.edu.agh.cs.to.cinemamak.service.TicketService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Predicate;

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

    @FXML
    private ChoiceBox<Genre> genreFilterChoiceBox;

    @FXML
    private Button clearGenreSelectionButton;

    private final PerformanceService performanceService;
    private final RoomService roomService;
    private final TicketService ticketService;
    private final MovieService movieService;

    private final ObjectProperty<Optional<Seat>> selectedSeat = new SimpleObjectProperty<>(Optional.empty());
    private final ObjectProperty<Optional<Genre>> selectedGenre = new SimpleObjectProperty<>(Optional.empty());

    public TicketsController(PerformanceService performanceService, RoomService roomService, TicketService ticketService, MovieService movieService) {
        this.performanceService = performanceService;
        this.roomService = roomService;
        this.ticketService = ticketService;
        this.movieService = movieService;
    }

    public void initialize() {
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMovie().getTitle()));

        dateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDate()
                .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))));

        priceColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPrice()));

        roomColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getRoom().getName()));

        performancesList.setItems(getPerformances());

        performancesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setSeatValues(newValue.getRoom());
            }
        });

        sellButton.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedSeat.getValue().isEmpty()
                || ticketService.isSeatTaken(getSelectedPerformance(), selectedSeat.getValue().get()),
                selectedSeat,
                performancesList.getSelectionModel().selectedItemProperty()));

        cancelReservationButton.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedSeat.getValue().isEmpty()
                        || !ticketService.isSeatTaken(getSelectedPerformance(), selectedSeat.getValue().get()),
                selectedSeat,
                performancesList.getSelectionModel().selectedItemProperty()));

        genreFilterChoiceBox.setItems(getGenres());
        genreFilterChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Genre object) {
                if (object == null) {
                    return "";
                }
                return object.getGenreName();
            }

            @Override
            public Genre fromString(String string) {
                return movieService.getGenreByName(string).orElse(null);
            }
        });
        genreFilterChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedGenre.setValue(Optional.of(newValue));
            } else {
                selectedGenre.setValue(Optional.empty());
            }
        });

        clearGenreSelectionButton.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedGenre.getValue().isEmpty(), selectedGenre));
    }

    private ObservableList<Genre> getGenres() {
        ObservableList<Genre> res = FXCollections.observableArrayList();
        movieService.getGenres().ifPresent(res::addAll);
        return res;
    }

    private ObservableList<Performance> getPerformances() {
        ObservableList<Performance> res = FXCollections.observableArrayList(performanceService.getPerformancesAfterToday());
        var filtered = res.filtered(p -> true);

        var predicateBinding = Bindings.createObjectBinding(() -> (Predicate<Performance>) performance ->
                selectedGenre.getValue().isEmpty() ||
                performance.getMovie().getGenre().getId() == selectedGenre.getValue().get().getId(), selectedGenre);
        filtered.predicateProperty().bind(predicateBinding);
        return filtered;
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
            var t = new Ticket(getSelectedPerformance(), selectedSeat.getValue().get(), LocalDateTime.now());
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
    @FXML
    private void clearGenreSelection() {
        genreFilterChoiceBox.getSelectionModel().clearSelection();
    }
}
