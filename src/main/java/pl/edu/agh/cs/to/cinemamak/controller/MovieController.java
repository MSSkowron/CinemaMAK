package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableView<Movie> tableView;
    @FXML
    private TableColumn<Movie, Integer> tableColumnID;
    @FXML
    private TableColumn<Movie, String> tableColumnTitle;
    @FXML
    private TableColumn<Movie, String> tableColumnDirector;
    private Stage stage;
    private final MovieService movieService;
    private final FxWeaver fxWeaver;

    public MovieController(MovieService movieService, FxWeaver fxWeaver) {
        this.movieService = movieService;
        this.fxWeaver = fxWeaver;
    }

    public void initialize() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("id"));
        tableColumnTitle.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
        tableColumnDirector.setCellValueFactory(new PropertyValueFactory<Movie, String>("director"));

        tableView.setItems(getMovies());
    }

    private ObservableList<Movie> getMovies() {
        ObservableList<Movie> movies = FXCollections.observableArrayList();

        movieService.getMovies().ifPresent(movies::addAll);

        return movies;
    }

    @FXML
    private void newMovie(javafx.scene.input.MouseEvent mouseEvent) {
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
