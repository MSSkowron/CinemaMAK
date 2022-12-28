package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ApplicationListener;
import org.springframework.data.convert.PropertyValueConverter;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.event.NewMovieAddedEvent;
import pl.edu.agh.cs.to.cinemamak.event.NewPerformanceAddedEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.model.Room;
import pl.edu.agh.cs.to.cinemamak.model.User;
import pl.edu.agh.cs.to.cinemamak.service.PerformanceService;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;

import java.math.BigDecimal;
import java.util.List;

@Component
@FxmlView("performance-view.fxml")
public class PerformanceController implements ApplicationListener<NewPerformanceAddedEvent> {

    @FXML
    private TableView<Performance> table;
    @FXML
    private TableColumn<Performance, String> columnTitle;
    @FXML
    private TableColumn<Performance, String> columnDate;
    @FXML
    private TableColumn<Performance, String> columnRoom;
    @FXML
    private TableColumn<Performance, String> columnSupervisor;
    @FXML
    private TableColumn<Performance, BigDecimal> columnPrice;
    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;

    private final SessionService sessionService;
    private final PerformanceService performanceService;
    private final FxWeaver fxWeaver;
    private Stage stage;
//    ObservableList<Performance> performanceObservableList;
//    List<Performance> performanceList;

    public PerformanceController(PerformanceService performanceService,SessionService sessionService, FxWeaver fxWeaver){
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
        this.performanceService = performanceService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(){

        this.columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.columnTitle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Performance, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Performance, String> param) {
                if(param.getValue().getMovie() != null) {
                    return new SimpleStringProperty(param.getValue().getMovie().getTitle());
                }
                else{
                    return new SimpleStringProperty("null");
                }
            }
        });

        this.columnRoom.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Performance, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Performance, String> param) {
                if(param.getValue().getRoom() != null){
                    return new SimpleStringProperty(param.getValue().getRoom().getName());
                }
                else{
                    return new SimpleStringProperty("null");
                }
            }
        });

        this.columnSupervisor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Performance, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Performance, String> param) {
                if(param.getValue().getUser() != null){
                    return new SimpleStringProperty(param.getValue().getUser().getFirstName() + " " + param.getValue().getUser().getLastName());
                }
                else{
                    return new SimpleStringProperty("null");
                }
            }
        });

        setPerformances();

    }

    public ObservableList<Performance> getPerformances(){
        ObservableList<Performance> performanceObservableList = FXCollections.observableArrayList();
        this.performanceService.getPerformances().ifPresent(performanceObservableList::addAll);
        return performanceObservableList;
    }

    public void setPerformances(){
        FilteredList<Performance> filteredList = new FilteredList<>(getPerformances(), p -> true);
        SortedList<Performance> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(this.table.comparatorProperty());

        this.table.setItems(sortedList);
    }

    public void setAddButton(){
        Stage form = new Stage();
        fxWeaver.loadController(PerformanceFormViewController.class).setStage(form);
        Scene scene = new Scene(fxWeaver.loadView(PerformanceFormViewController.class));
        form.setScene(scene);
        form.setTitle("Add performance");
        form.initModality(Modality.WINDOW_MODAL);
        form.setAlwaysOnTop(true);
        form.initOwner(stage);
        form.show();
    }

    public void setDeleteButton(){
        Performance performance = this.table.getSelectionModel().getSelectedItem();
        this.performanceService.deletePerformanceById(performance.getId());
        this.table.getItems().remove(performance);
    }

    @Override
    public void onApplicationEvent(NewPerformanceAddedEvent event) {
        setPerformances();
        this.table.refresh();
    }
}