package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.event.NewMovieAddedEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;

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

    private final FxWeaver fxWeaver;
    private Stage stage;
    private final MovieService movieService;
    private Movie movie;

    public MovieDetailsController(MovieService movieService, FxWeaver fxWeaver) {
        this.movieService = movieService;
        this.fxWeaver = fxWeaver;
    }

    public void initialize() {
        labelTitle.textProperty().bind(Bindings.createStringBinding(() -> movie.getTitle()));
        labelDirector.textProperty().bind(Bindings.createStringBinding(() -> movie.getDirector()));
        labelDate.textProperty().bind(Bindings.createStringBinding(() -> String.valueOf(movie.getDate())));
        labelDuration.textProperty().bind(Bindings.createStringBinding(() -> movie.getDuration() + " min"));
        labelGenre.textProperty().bind(Bindings.createStringBinding(() -> movie.getGenre().getGenreName()));
        textAreaDescription.textProperty().bind(Bindings.createStringBinding(() -> movie.getDescription()));
    }

    public void setStage(Stage s) {
        this.stage = s;
    }
    public void setMovie(Movie m) {
        this.movie = m;
    }

    public void onButtonDelete(MouseEvent event) {
        movieService.deleteMovie(movie);

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
