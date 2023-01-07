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
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;
import pl.edu.agh.cs.to.cinemamak.service.PerformanceService;
import pl.edu.agh.cs.to.cinemamak.service.RecommendationService;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@FxmlView("recommendations-view.fxml")
public class RecommendationsController extends ExtractedTableController<Recommendation> implements ApplicationListener<TableRecommendationsChangeEvent> {

    public Button resetButton;
    @FXML
    private TableColumn<Recommendation, String> columnTitle;
    @FXML
    private TableColumn<Recommendation, String> columnDateBegin;
    @FXML
    private TableColumn<Recommendation, String> columnDateEnd;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;

    private final SessionService sessionService;
    private final MovieService movieService;
    private final FxWeaver fxWeaver;

    public RecommendationsController(MovieService movieService, RecommendationService recommendationService, SessionService sessionService, FxWeaver fxWeaver){
        super();
        super.setService(recommendationService);
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
        this.movieService = movieService;
    }


    public void initialize(){
        super.initialize();

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
        movieService.getGenres().ifPresent(listM -> listM.forEach(genre -> this.genreChoiceBox.getItems().add(genre.getGenreName())));

        setEntities(r -> true);
    }

    public void onMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            Stage newStage = new Stage();
            if(this.table.getSelectionModel().getSelectedItem() == null) return;
            fxWeaver.loadController(RecommendationsEditController.class).setRecommendation(this.table.getSelectionModel().getSelectedItem()).setStage(newStage);

            Scene newScene = new Scene(fxWeaver.loadView(RecommendationsEditController.class));

            newStage.setTitle("Edit recommendation");
            newStage.setScene(newScene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(stage);

            newStage.show();
        }
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
        deleteEntity();
        applicationEventPublisher.publishEvent(new TableRecommendationsChangeEvent(this));
    }

    @Override
    public void onApplicationEvent(TableRecommendationsChangeEvent event) {
        resetTable();
        cleanFields();
    }

    public void OnActionReset(ActionEvent actionEvent) {
        resetTable();
        cleanFields();
    }


}
