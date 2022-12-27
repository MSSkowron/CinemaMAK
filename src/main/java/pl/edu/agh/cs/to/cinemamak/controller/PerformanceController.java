package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.data.convert.PropertyValueConverter;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;

import java.util.List;

@Component
@FxmlView("performance-view.fxml")
public class PerformanceController {

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
    public Button addButton;
    @FXML
    public Button deleteButton;

    private final SessionService sessionService;
    private final FxWeaver fxWeaver;
    private Stage stage;
    ObservableList<Performance> performanceObservableList;
    List<Performance> performanceList;

    public PerformanceController(SessionService sessionService, FxWeaver fxWeaver){
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(){

        this.columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.columnSupervisor.setCellValueFactory(new PropertyValueFactory<>("supervisor"));
        this.columnRoom.setCellValueFactory(new PropertyValueFactory<>("room"));

        ObservableList<Performance> performanceObservableList1 = FXCollections.observableArrayList();

        FilteredList<Performance> filteredList = new FilteredList<>(performanceObservableList1);
//        this.table.getColumns().setAll(columnTitle, columnDate, columnRoom, columnSupervisor);
//        performanceList = List.of(new Performance("title1", "date1", "room1", "sup1"));
//        performanceObservableList = FXCollections.observableArrayList(performanceList);
//        this.table.setItems(performanceObservableList);
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
        this.table.getItems().remove(this.table.getSelectionModel().getSelectedItem());
    }

}