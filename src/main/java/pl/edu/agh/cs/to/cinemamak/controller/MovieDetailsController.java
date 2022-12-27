package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.event.NewMovieAddedEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;

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
    private final ObjectProperty<Optional<Movie>> movie = new SimpleObjectProperty<>(Optional.empty());

    public MovieDetailsController(MovieService movieService) {
        this.movieService = movieService;
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

        movieService.deleteMovie(movie.get());

        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Deletion success");
        dialog.setHeaderText("Movie deleted successfully!");
        dialog.show();
        dialog.setOnCloseRequest(e -> {
            applicationEventPublisher.publishEvent(new NewMovieAddedEvent(this));
            stage.close();
        });
    }
}
