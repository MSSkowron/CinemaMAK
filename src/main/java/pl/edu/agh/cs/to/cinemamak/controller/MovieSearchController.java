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
import pl.edu.agh.cs.to.cinemamak.event.MovieSelectedEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@FxmlView("movie-search-view.fxml")
public class MovieSearchController {
    @FXML
    public TextField titleTextField;
    @FXML
    public TextField directorTextField;
    @FXML
    public TextField yearTextField;
    @FXML
    public ChoiceBox<String> genreChoiceBox;
    @FXML
    public Button searchButton;
    @FXML
    public TableView<Movie> table;
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
    private Stage stage;
    private Optional<Movie> selectedMovie = Optional.empty();

    public MovieSearchController(MovieService movieService, FxWeaver fxWeaver){
        this.movieService = movieService;
        this.fxWeaver = fxWeaver;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setSelectedMovie(Movie mv){ this.selectedMovie = Optional.of(mv); }

    public void initialize(){
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        genreColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                if(param.getValue().getGenre() != null){
                    return new SimpleStringProperty(param.getValue().getGenre().getGenreName());
                }
                else{
                    return new SimpleStringProperty("null");
                }
            }
        });
        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                if(param.getValue().getDate() != null){
                    return new SimpleStringProperty(String.valueOf(param.getValue().getDate().toLocalDate().getYear()));
                }
                else{
                    return new SimpleStringProperty("null");
                }
            }
        });
        movieService.getGenres().ifPresent(listM -> listM.forEach(genre -> this.genreChoiceBox.getItems().add(genre.getGenreName())));
        setMovies(m -> true);
    }

    private ObservableList<Movie> getMovies() {
        ObservableList<Movie> movies = FXCollections.observableArrayList();

        movieService.getMovies().ifPresent(movies::addAll);

        return movies;
    }

    private void setMovies(Predicate<Movie> moviePredicate) {
        FilteredList<Movie> filteredList = new FilteredList<>(getMovies(), moviePredicate);
        SortedList<Movie> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    public void onMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            this.OnActionApply(new ActionEvent());
        }
    }


    public void OnActionApply(ActionEvent actionEvent) {
        Movie mv = this.table.getSelectionModel().getSelectedItem();
        if(mv == null){
            showErrorDialog("Error occurred while applying a movie!",
                    "Please select one movie from the table.");
            return;
        }

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
        applicationEventPublisher.publishEvent(new MovieSelectedEvent(this));
        this.stage.close();
    }

    public void OnActionReset(ActionEvent actionEvent) {
        setMovies(m -> true);
        this.genreChoiceBox.setValue("");
        this.directorTextField.setText("");
        this.titleTextField.setText("");
        this.yearTextField.setText("");
    }

    public void OnActionCancel(ActionEvent actionEvent) {
        this.stage.close();
    }

    public void onActionSearch(ActionEvent actionEvent) {

        String title = titleTextField.getText();
        String director = directorTextField.getText();
        String year = yearTextField.getText();
        String genre = genreChoiceBox.getValue();

        setMovies(new Predicate<Movie>() {
            @Override
            public boolean test(Movie movie) {
                if(!title.equals("")){

                    Pattern pattern = Pattern.compile(title, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(movie.getTitle());
                    if(!matcher.find()){
                        return false;
                    }
                }
                if(!director.equals("")){

                    Pattern pattern = Pattern.compile(director, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(movie.getDirector());
                    if(!matcher.find()){
                        return false;
                    }
                }
                if(!year.equals("")) {

                    try {
                        if (!Integer.valueOf(movie.getDate().getYear()).equals(Integer.valueOf(year))) {
                            return false;
                        }
                    } catch(NumberFormatException exception){
                        showErrorDialog("Error occurred while filtering list of movies.",
                                "Year is not valid! Enter a number or left it empty.");
                    }
                }
                if(!genre.equals("")){

                    if(!movie.getGenre().getGenreName().equals(genre)){
                        return false;
                    }
                }
                return true;
            }
        });

    }

    public void showErrorDialog(String header, String info){
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Error");
        dialog.setHeaderText(header);
        dialog.setContentText(info);
        dialog.show();
    }
}
