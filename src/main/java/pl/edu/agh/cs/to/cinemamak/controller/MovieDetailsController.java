package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.config.DialogManager;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import pl.edu.agh.cs.to.cinemamak.event.TableMovieChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;
import pl.edu.agh.cs.to.cinemamak.service.PerformanceService;
import pl.edu.agh.cs.to.cinemamak.service.RecommendationService;

import java.util.List;
import java.util.Optional;

@Component
@FxmlView("movie-details-view.fxml")
public class MovieDetailsController {
    @FXML
    ImageView imageView;
    @FXML
    Label labelTitle;
    @FXML
    Label labelDirector;
    @FXML
    Label labelDate;
    @FXML
    Label labelDuration;
    @FXML
    Label labelGenre;
    @FXML
    TextArea textAreaDescription;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private Stage stage;
    private final MovieService movieService;
    private final PerformanceService performanceService;
    private final DialogManager dialogManager;
    private final RecommendationService recommendationService;
    private final ObjectProperty<Optional<Movie>> movie = new SimpleObjectProperty<>(Optional.empty());

    public MovieDetailsController(DialogManager dialogManager,
                                  PerformanceService performanceService,
                                  MovieService movieService,
                                  RecommendationService recommendationService) {
        this.movieService = movieService;
        this.performanceService = performanceService;
        this.dialogManager = dialogManager;
        this.recommendationService = recommendationService;
    }

    public void initialize() {
        imageView.imageProperty().bind(Bindings.createObjectBinding(() -> {
            if (getMovie().isEmpty()) {
                return null;
            }

            Movie movie = getMovie().get();
            return new Image(movie.getImageURL());
        }, getMovieProperty()));

        labelTitle.textProperty().bind(Bindings.createStringBinding(() -> {
            if (getMovie().isEmpty()) {
                return "";
            }

            Movie movie = getMovie().get();
            return movie.getTitle();
        }, getMovieProperty()));

        labelDirector.textProperty().bind(Bindings.createStringBinding(() -> {
            if (getMovie().isEmpty()) {
                return "";
            }

            Movie movie = getMovie().get();
            return movie.getDirector();
        }, getMovieProperty()));

        labelDate.textProperty().bind(Bindings.createStringBinding(() -> {
            if (getMovie().isEmpty()) {
                return "";
            }

            Movie movie = getMovie().get();
            return String.valueOf(movie.getDate());
        }, getMovieProperty()));

        labelDuration.textProperty().bind(Bindings.createStringBinding(() -> {
            if (getMovie().isEmpty()) {
                return "";
            }

            Movie movie = getMovie().get();
            return movie.getDuration() + " min" ;
        }, getMovieProperty()));

        labelGenre.textProperty().bind(Bindings.createStringBinding(() -> {
            if (getMovie().isEmpty()) {
                return "";
            }

            Movie movie = getMovie().get();
            return movie.getGenre().getGenreName();
        }, getMovieProperty()));

        textAreaDescription.textProperty().bind(Bindings.createStringBinding(() -> {
            if (getMovie().isEmpty()) {
                return "";
            }

            Movie movie = getMovie().get();
            return movie.getDescription();
        }, getMovieProperty()));
    }

    public void setStage(Stage s) {
        stage = s;
    }

    public void setMovie(Movie m) {
        movie.set(Optional.of(m));
    }

    public Optional<Movie> getMovie() {
        return movie.get();
    }

    public ObjectProperty<Optional<Movie>> getMovieProperty() {
        return movie;
    }

    public void onButtonDelete(MouseEvent event) {
        Optional<Movie> movie = getMovie();
        if (movie.isEmpty()) {
            return;
        }
        Optional<List<Performance>> listPerformances = lookForAssociatedPerformances(movie.get());
        Optional<List<Recommendation>> listRecommendations = lookForAssociatedRecommendations(movie.get());

        boolean associatedPerformancesExists = listPerformances.isPresent() && !listPerformances.get().isEmpty();
        boolean associatedRecommendationsExists = listRecommendations.isPresent() && !listRecommendations.get().isEmpty();

        if(associatedPerformancesExists){
            boolean approveForPerformancesRemoval = this.dialogManager.askForConfirmation(
                stage, "Movie belongs to some performances.",
                "Do you want to delete all associated performances?");
            if(!approveForPerformancesRemoval) return;
        }

        if(associatedRecommendationsExists){
            boolean approveForRecommendationsRemoval = this.dialogManager.askForConfirmation(
                    stage, "Movie belongs to some recommendations.",
                    "Do you want to delete all associated recommendations?");
            if(!approveForRecommendationsRemoval) return;
        }

        if(associatedPerformancesExists){
            deleteAllAssociatedPerformances(listPerformances.get());
        }

        if(associatedRecommendationsExists){
            deleteAllAssociatedRecommendations(listRecommendations.get());
        }

        this.deleteMovie(movie.get());

        this.dialogManager.showInformation(stage, "Movie deleted successfully!", "", e -> {
            applicationEventPublisher.publishEvent(new TableMovieChangeEvent(this));
            stage.close();
        });

    }

    private Optional<List<Performance>>  lookForAssociatedPerformances(Movie movie){
        return performanceService.getPerformancesByMovieId(movie.getId());
    }

    private Optional<List<Recommendation>>  lookForAssociatedRecommendations(Movie movie){
        return recommendationService.getEntitiesByMovieId(movie.getId());
    }

    private void deleteAllAssociatedPerformances(List<Performance> listPerformances){
        for(Performance p: listPerformances) {
            performanceService.deletePerformanceById(p.getId());
        }
    }

    private void deleteAllAssociatedRecommendations(List<Recommendation> listRecommendations){
        for(Recommendation r: listRecommendations) {
            recommendationService.deleteEntityById(r.getId());
        }
    }

    private void deleteMovie(Movie movie){
        movieService.deleteMovie(movie);
        applicationEventPublisher.publishEvent(new TableMovieChangeEvent(this));
    }

}
