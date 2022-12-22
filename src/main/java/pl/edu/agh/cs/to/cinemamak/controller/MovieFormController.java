package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.Genre;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Component
@FxmlView("movie-form-view.fxml")
public class MovieFormController {
    private final MovieService movieService;

    @FXML
    public TextField textFieldTitle;
    @FXML
    public TextField textFieldDirector;
    @FXML
    public TextArea textAreaDescription;
    @FXML
    public TextField textFieldDuration;
    @FXML
    public ChoiceBox<String> choiceBoxGenre;
    @FXML
    public DatePicker datePicker;
    @FXML
    public TextField textFieldImage;
    @FXML
    public Button buttonSubmit;
    private Stage stage;

    public MovieFormController(MovieService movieService) {
        this.movieService = movieService;
    }

    public void initialize() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        textFieldDuration.setTextFormatter(new TextFormatter<String>(integerFilter));

        movieService.getGenres().ifPresent( listM -> listM.forEach(genre -> this.choiceBoxGenre.getItems().add(genre.getGenreName())));
    }
    @FXML
    public void onButtonSubmit(MouseEvent actionEvent) {
        String title = this.textFieldTitle.getCharacters().toString();
        String director = this.textFieldDirector.getCharacters().toString();
        String description = this.textAreaDescription.getText();
        int duration = Integer.parseInt(this.textFieldDuration.getCharacters().toString());
        String genreName = choiceBoxGenre.getValue();
        LocalDate date =  datePicker.getValue();
        String image = this.textFieldImage.getCharacters().toString();

        Optional<Genre> genre = movieService.getGenreByName(genreName);
        if (genre.isPresent()) {
            Movie movie = new Movie(title, director, description, duration, genre.get(), Date.valueOf(date));
            movieService.addMovie(movie);

            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Information");
            dialog.setHeaderText("New movie added successfully!");
            dialog.show();
            dialog.setOnCloseRequest(event -> {
                stage.close();
            });
        }
    }

    public void setStage(Stage s) {
        this.stage = s;
    }
}
