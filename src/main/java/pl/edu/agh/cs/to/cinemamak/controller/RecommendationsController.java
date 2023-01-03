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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import pl.edu.agh.cs.to.cinemamak.event.TablePerformanceChangeEvent;
import pl.edu.agh.cs.to.cinemamak.event.TableRecommendationsChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.service.PerformanceService;
import pl.edu.agh.cs.to.cinemamak.service.RecommendationService;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Component
@FxmlView("recommendations-view.fxml")
public class RecommendationsController implements ApplicationListener<TableRecommendationsChangeEvent> {


    @FXML
    private TableView<Recommendation> table;
    @FXML
    private TableColumn<Recommendation, String> columnTitle;
    @FXML
    private TableColumn<Recommendation, String> columnDateBegin;
    @FXML
    private TableColumn<Recommendation, String> columnDateEnd;

    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final SessionService sessionService;
    private final RecommendationService recommendationService;
    private final FxWeaver fxWeaver;
    private Stage stage;

    public RecommendationsController(RecommendationService recommendationService,SessionService sessionService, FxWeaver fxWeaver){
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
        this.recommendationService = recommendationService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void initialize(){
        this.columnDateBegin.setCellValueFactory(param -> {
            if(param.getValue().getDateFrom() != null) {
                return new SimpleStringProperty(param.getValue().getDateFrom().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            }
            else{
                return new SimpleStringProperty("null");
            }
        });

        this.columnDateEnd.setCellValueFactory(param -> {
            if(param.getValue().getDateTo() != null) {
                return new SimpleStringProperty(param.getValue().getDateTo().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
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

        setRecommendations();
    }

    public void onMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
//            Stage newStage = new Stage();
//
//            fxWeaver.loadController(PerformanceEditController.class).setPerformance(this.table.getSelectionModel().getSelectedItem()).setStage(newStage);
//
//            Scene newScene = new Scene(fxWeaver.loadView(PerformanceEditController.class));
//
//            newStage.setTitle("Edit performance");
//            newStage.setScene(newScene);
//            newStage.initModality(Modality.WINDOW_MODAL);
//            newStage.initOwner(stage);
//
//            newStage.show();
        }
    }

    public ObservableList<Recommendation> getRecommendations(){
        ObservableList<Recommendation> recommendationObservableList = FXCollections.observableArrayList();
        this.recommendationService.getRecommendations().ifPresent(recommendationObservableList::addAll);
        return recommendationObservableList;
    }

    public void setRecommendations(){
        FilteredList<Recommendation> filteredList = new FilteredList<>(getRecommendations(), p -> true);
        SortedList<Recommendation> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(this.table.comparatorProperty());

        this.table.setItems(sortedList);
    }

    public void setAddButton(){
        Stage form = new Stage();
        fxWeaver.loadController(RecommendationsFormController.class).setStage(form);
        Scene scene = new Scene(fxWeaver.loadView(RecommendationsFormController.class));
        form.setScene(scene);
        form.setTitle("Add recommendation");
        form.initModality(Modality.WINDOW_MODAL);
        form.setAlwaysOnTop(true);
        form.initOwner(stage);
        form.show();
    }

    public void setDeleteButton(){
        Recommendation recommendation = this.table.getSelectionModel().getSelectedItem();
        this.recommendationService.deleteRecommendationById(recommendation.getId());
        applicationEventPublisher.publishEvent(new TableRecommendationsChangeEvent(this));
    }

    @Override
    public void onApplicationEvent(TableRecommendationsChangeEvent event) {
        setRecommendations();
        this.table.refresh();
    }
}
