package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;

import java.util.List;

@Component
@FxmlView("performance-view.fxml")
public class PerformanceController {

    public static class Performance{
        private StringProperty title;
        public void setTitle(String strTitle){ this.title.setValue(strTitle); }
        public String getTitle(){ return this.title.get(); }
        public StringProperty titleProperty(){
            if( this.title == null) this.title = new SimpleStringProperty("title");
            return this.title;
        }

        private StringProperty date;
        public void setDate(String strDate){ this.date.setValue(strDate);}
        public String getDate(){ return this.date.get(); }
        public StringProperty dateProperty(){
            if(this.date == null) this.date = new SimpleStringProperty("date");
            return this.date;
        }

        private StringProperty room;
        public void setRoom(String strRoom){ this.room.setValue(strRoom);}
        public String getRoom(){ return this.room.get(); }
        public StringProperty roomProperty(){
            if(this.room == null) this.room = new SimpleStringProperty("0");
            return this.room;
        }

        private StringProperty supervisor;
        public void setSupervisor(String strSup){ this.supervisor.setValue(strSup);}
        public String getSupervisor(){ return this.supervisor.get(); }
        public StringProperty supervisorProperty(){
            if(this.supervisor == null) this.supervisor = new SimpleStringProperty("supervisor");
            return this.supervisor;
        }

        public Performance(String title, String date, String room, String supervisor){
            this.supervisor = new SimpleStringProperty(supervisor);
            this.date = new SimpleStringProperty(date);
            this.room = new SimpleStringProperty(room);
            this.title = new SimpleStringProperty(title);
        }
    }

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

    public PerformanceController(SessionService sessionService, FxWeaver fxWeaver){
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
//        this.stage = new Stage();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(){
        List<Performance> performanceList = List.of(new Performance("title1", "date1", "room1", "sup1"));
        ObservableList<Performance> performanceObservableList = FXCollections.observableArrayList(performanceList);
        this.table.setItems(performanceObservableList);
    }

    public void setAddButton(){
        if(this.stage == null) return;
        Scene scene = new Scene(fxWeaver.loadView(PerformanceFormViewController.class));
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void setDeleteButton(){

    }

}
