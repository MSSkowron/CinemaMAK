package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.config.DialogManager;
import pl.edu.agh.cs.to.cinemamak.event.TablePerformanceChangeEvent;
import pl.edu.agh.cs.to.cinemamak.event.TableRecommendationsChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;
import pl.edu.agh.cs.to.cinemamak.service.PerformanceService;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@FxmlView("performance-view.fxml")
public class PerformanceController extends ExtractedTableController<Performance>  implements ApplicationListener<TablePerformanceChangeEvent> {

    public Button resetButton;
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


    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private final SessionService sessionService;
    private final MovieService movieService;
    private final FxWeaver fxWeaver;

    public PerformanceController(MovieService movieService,PerformanceService performanceService, SessionService sessionService, FxWeaver fxWeaver){
        super(movieService);
        super.setService(performanceService);
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
        this.movieService = movieService;
    }

    public void initialize(){

        super.initialize();

        this.columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        this.columnDate.setCellValueFactory(param -> {
            if(param.getValue().getDate() != null) {
                return new SimpleStringProperty(param.getValue().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            }
            else{
                return new SimpleStringProperty("null");
            }
        });

        this.columnTitle.setCellValueFactory(param -> {
            if(param.getValue().getMovie() != null) {
                return new SimpleStringProperty(param.getValue().getMovie().getTitle());
            }
            else{
                return new SimpleStringProperty("null");
            }
        });

        this.columnRoom.setCellValueFactory(param -> {
            if(param.getValue().getRoom() != null){
                return new SimpleStringProperty(param.getValue().getRoom().getName());
            }
            else{
                return new SimpleStringProperty("null");
            }
        });

        this.columnSupervisor.setCellValueFactory(param -> {
            if(param.getValue().getUser() != null){
                return new SimpleStringProperty(param.getValue().getUser().getFirstName() + " " + param.getValue().getUser().getLastName());
            }
            else{
                return new SimpleStringProperty("null");
            }
        });
        movieService.getGenres().ifPresent(listM -> listM.forEach(genre -> this.genreChoiceBox.getItems().add(genre.getGenreName())));

    }

    public void onMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            Stage newStage = new Stage();
            if(this.table.getSelectionModel().getSelectedItem() == null) return;
            fxWeaver.loadController(PerformanceEditController.class).setPerformance(this.table.getSelectionModel().getSelectedItem()).setStage(newStage);

            Scene newScene = new Scene(fxWeaver.loadView(PerformanceEditController.class));

            newStage.setTitle("Edit performance");
            newStage.setScene(newScene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(stage);

            newStage.show();
        }
    }


    public void setAddButton(){
        Stage form = new Stage();
        fxWeaver.loadController(PerformanceFormController.class).setStage(form);
        Scene scene = new Scene(fxWeaver.loadView(PerformanceFormController.class));
        form.setScene(scene);
        form.setTitle("Add performance");
        form.initModality(Modality.WINDOW_MODAL);
        form.setAlwaysOnTop(true);
        form.initOwner(stage);
        form.show();
    }

    public void setDeleteButton(){
        deleteEntity();
        applicationEventPublisher.publishEvent(new TablePerformanceChangeEvent(this));
    }

    @Override
    public void onApplicationEvent(TablePerformanceChangeEvent event) {
        resetTable();
        cleanFields();
    }


    public void OnActionReset(ActionEvent actionEvent) {
        resetTable();
        cleanFields();
    }

    public void onActionSearch(ActionEvent actionEvent) {
        searchByMovie();
    }
}