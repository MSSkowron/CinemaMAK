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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.config.DialogManager;
import pl.edu.agh.cs.to.cinemamak.event.MovieSelectedEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@FxmlView("movie-search-view.fxml")
public class MovieSearchController extends ExtractedTableController<Movie> {
    @FXML
    public TableColumn<Movie, String> titleColumn;
    @FXML
    public TableColumn<Movie, String> directorColumn;
    @FXML
    public TableColumn<Movie, String> genreColumn;
    @FXML
    public TableColumn<Movie, String> dateColumn;
    @FXML
    public Button applyButton;
    @FXML
    public Button resetButton;
    @FXML
    public Button cancelButton;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private final MovieService movieService;
    private final FxWeaver fxWeaver;
    private final DialogManager dialogManager;
    private Optional<Movie> selectedMovie = Optional.empty();

    public MovieSearchController(MovieService movieService, FxWeaver fxWeaver, DialogManager dialogManager){
        super(movieService);
        this.movieService = movieService;
        this.fxWeaver = fxWeaver;
        this.dialogManager = dialogManager;
        this.setService(movieService);
    }

    public void setSelectedMovie(Movie mv){ this.selectedMovie = Optional.of(mv); }

    public void initialize(){
        super.initialize();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        genreColumn.setCellValueFactory(param -> {
            if(param.getValue().getGenre() != null){
                return new SimpleStringProperty(param.getValue().getGenre().getGenreName());
            }
            else{
                return new SimpleStringProperty("null");
            }
        });
        dateColumn.setCellValueFactory(param -> {
            if(param.getValue().getDate() != null){
                return new SimpleStringProperty(String.valueOf(param.getValue().getDate().toLocalDate().getYear()));
            }
            else{
                return new SimpleStringProperty("null");
            }
        });
        movieService.getGenres().ifPresent(listM -> listM.forEach(genre -> this.genreChoiceBox.getItems().add(genre.getGenreName())));

    }

    public void onMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            this.OnActionApply(new ActionEvent());
        }
    }


    public void OnActionApply(ActionEvent actionEvent) {
        Movie mv = this.table.getSelectionModel().getSelectedItem();
        if(mv == null){
            this.dialogManager.showError(stage,"Error occurred while applying a movie!",
                    "Please select one movie from the table.");
            return;
        }

        this.applyMovie(mv);

        applicationEventPublisher.publishEvent(new MovieSelectedEvent(this));
        this.stage.close();
    }

    private void applyMovie(Movie mv){
        if(selectedMovie.isEmpty()) return;
        Movie selectedMovie = this.selectedMovie.get();
        selectedMovie.setGenre(mv.getGenre());
        selectedMovie.setDuration(mv.getDuration());
        selectedMovie.setDirector(mv.getDirector());
        selectedMovie.setTitle(mv.getTitle());
        selectedMovie.setDescription(mv.getDescription());
        selectedMovie.setDate(mv.getDate());
        selectedMovie.setImageURL(mv.getImageURL());
        selectedMovie.setId(mv.getId());
    }

    public void OnActionReset(ActionEvent actionEvent) {
        resetTable();
        cleanFields();
    }

    public void OnActionCancel(ActionEvent actionEvent) {
        this.stage.close();
    }

    public void onActionSearch(ActionEvent actionEvent) {
        searchByMovie();
    }
}
