package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.config.DialogManager;
import pl.edu.agh.cs.to.cinemamak.event.MovieSelectedEvent;
import pl.edu.agh.cs.to.cinemamak.event.TableRecommendationsChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;
import pl.edu.agh.cs.to.cinemamak.service.RecommendationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Component
@FxmlView("recommendations-edit-view.fxml")
public class RecommendationsEditController implements ApplicationListener<MovieSelectedEvent> {
    @FXML
    public DatePicker dateFromPicker;
    @FXML
    public DatePicker dateToPicker;
    public TextField textFieldMovie;
    public Button buttonSearch;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private final MovieService movieService;
    private final RecommendationService recommendationService;
    private final FxWeaver fxWeaver;
    private final DialogManager dialogManager;
    private Stage stage;
    private Optional<Recommendation> recommendation = Optional.empty();
    private Optional<Movie> selectedMovie = Optional.empty();

    public RecommendationsEditController(MovieService movieService, RecommendationService recommendationService, FxWeaver fxWeaver, DialogManager dialogManager) {
        this.movieService = movieService;
        this.recommendationService = recommendationService;
        this.fxWeaver = fxWeaver;
        this.dialogManager = dialogManager;
    }

    public RecommendationsEditController setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public RecommendationsEditController setRecommendation(Recommendation recommendation) {
        this.recommendation = Optional.of(recommendation);
        return this;
    }

    public void initialize() {
        if (recommendation.isPresent()) {
            this.setFields();
        }
    }

    public void setFields() {
        if (this.recommendation.isPresent()) {
            Recommendation recommendation = this.recommendation.get();

            this.dateFromPicker.setValue(recommendation.getDateFrom().toLocalDate());
            this.dateToPicker.setValue(recommendation.getDateTo().toLocalDate());
            this.textFieldMovie.setText(recommendation.getMovie().getTitle());
            this.selectedMovie = Optional.of(recommendation.getMovie());
        }
    }

    public void onActionApply(ActionEvent actionEvent) {
        if (this.selectedMovie.isEmpty()) {
            this.dialogManager.showError(stage,"Error occurred while editing a recommendation", "Movie must be chosen.");
            return;
        }

        String title = this.selectedMovie.get().getTitle();
        long movieId = this.selectedMovie.get().getId();
        LocalDate dateFrom = this.dateFromPicker.getValue();
        LocalDate dateTo = this.dateToPicker.getValue();

        if (dateTo != null && dateFrom != null && title != null) {
            Optional<Movie> movie = movieService.getMovieById(movieId);

            if (dateTo.isBefore(dateFrom)) {
                this.dialogManager.showError(stage,"Error occurred while editing a recommendation", "Begin date must be before end date.");
                return;
            }

            LocalTime time = LocalTime.of(0, 0, 0);
            LocalDateTime localDateTimeFrom = LocalDateTime.of(dateFrom, time);
            LocalDateTime localDateTimeTo = LocalDateTime.of(dateTo, time);

            if (movie.isPresent() && this.recommendation.isPresent()) {
                this.recommendation.get().setMovie(movie.get());
                this.recommendation.get().setDateFrom(localDateTimeFrom);
                this.recommendation.get().setDateTo(localDateTimeTo);
                this.recommendationService.addEntity(this.recommendation.get());

                this.dialogManager.showInformation(stage, "Recommendation edited successfully","", event -> {
                    applicationEventPublisher.publishEvent(new TableRecommendationsChangeEvent(this));
                    stage.close();
                });
            } else {
                this.dialogManager.showError(stage,"Error occurred while editing a recommendation", "All fields need to be filled!");
            }
        } else {
            this.dialogManager.showError(stage,"Error occurred while editing a recommendation", "All fields need to be filled!");
        }
    }

    public void onActionCancel(ActionEvent actionEvent) {
        this.stage.close();
    }

    public void onActionSearch(ActionEvent actionEvent) {
        Stage stageMovieSearch = new Stage();

        this.selectedMovie = Optional.of(new Movie());

        fxWeaver.loadController(MovieSearchController.class).setStage(stageMovieSearch);
        fxWeaver.loadController(MovieSearchController.class).setSelectedMovie(this.selectedMovie.get());

        Scene scene = new Scene(fxWeaver.loadView(MovieSearchController.class));

        stageMovieSearch.setScene(scene);
        stageMovieSearch.setTitle("Movie search");
        stageMovieSearch.initModality(Modality.WINDOW_MODAL);
        stageMovieSearch.setAlwaysOnTop(true);
        stageMovieSearch.initOwner(stage);
        stageMovieSearch.show();
    }

    @Override
    public void onApplicationEvent(MovieSelectedEvent event) {
        if (this.selectedMovie.isEmpty()) {
            return;
        }

        this.textFieldMovie.setText(this.selectedMovie.get().getTitle());
    }
}
