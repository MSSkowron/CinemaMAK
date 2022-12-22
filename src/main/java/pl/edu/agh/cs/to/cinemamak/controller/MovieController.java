package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;

@Component
@FxmlView("movie-view.fxml")
public class MovieController {
    @FXML
    private TableView tableView;
    private Stage stage;
    private final MovieService movieService;
    private final FxWeaver fxWeaver;

    public MovieController(MovieService movieService, FxWeaver fxWeaver) {
        this.movieService = movieService;
        this.fxWeaver = fxWeaver;
    }

    public void initialize() {
    }

    @FXML
    private void newMovie(javafx.scene.input.MouseEvent mouseEvent) {
        movieService.getMovies().ifPresent( listM -> listM.forEach(movie -> System.out.println(movie.getTitle())));

        Stage formStage = new Stage();
        fxWeaver.loadController(MovieFormController.class).setStage(formStage);

        Scene formScene = new Scene(fxWeaver.loadView(MovieFormController.class));

        formStage.setTitle("CinemaMAK-NewMovieForm");
        formStage.setScene(formScene);
        formStage.initModality(Modality.WINDOW_MODAL);
        formStage.initOwner(stage);

        formStage.show();
    }

    public void setStage(Stage s) {
        this.stage = s;
    }

}
