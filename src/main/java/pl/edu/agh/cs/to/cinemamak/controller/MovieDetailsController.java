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
import pl.edu.agh.cs.to.cinemamak.service.MovieService;
import pl.edu.agh.cs.to.cinemamak.service.PerformanceService;

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
    private final ObjectProperty<Optional<Movie>> movie = new SimpleObjectProperty<>(Optional.empty());

    public MovieDetailsController(DialogManager dialogManager, PerformanceService performanceService, MovieService movieService) {
        this.movieService = movieService;
        this.performanceService = performanceService;
        this.dialogManager = dialogManager;
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
        Optional<List<Performance>> listPerf = performanceService.
                getPerformancesByMovieId(movie.get().getId());
        if(listPerf.isPresent() && !listPerf.get().isEmpty()){

            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Error during delete operation.");
            dialog.setHeaderText("Movie belongs to some performances.");
            dialog.setContentText("Do you want to delete all associated performances?");
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent()){
                if(result.get() == ButtonType.OK){
                    for(Performance p: listPerf.get()) {
                        performanceService.deletePerformanceById(p.getId());
                    }
                    movieService.deleteMovie(movie.get());
                    applicationEventPublisher.publishEvent(new TableMovieChangeEvent(this));
                    stage.close();
                }
                else{
                    return;
                }
            }
        }

        movieService.deleteMovie(movie.get());

        this.dialogManager.setDialogInformation(stage, "Movie deleted successfully!", e -> {
            applicationEventPublisher.publishEvent(new TableMovieChangeEvent(this));
            stage.close();
        });

    }
}
