package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.User;

@Component
@FxmlView("movie-details-view.fxml")
public class MovieDetailsController {
    @FXML
    public Label labelId;
    @FXML
    public Label labelTitle;
    @FXML
    public Label labelDirector;
    private Stage stage;
    private Movie movie;

    public MovieDetailsController() {
    }

    public void initialize() {
        labelId.textProperty().bind(Bindings.createStringBinding(() -> String.valueOf(movie.getId())));
        labelTitle.textProperty().bind(Bindings.createStringBinding(() -> movie.getTitle()));
        labelDirector.textProperty().bind(Bindings.createStringBinding(() -> movie.getDirector()));
    }

    public void setStage(Stage s) {
        this.stage = s;
    }
    public void setMovie(Movie m) {
        this.movie = m;
    }
}
