package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.event.ControlPanelSelectionChangeEvent;
import pl.edu.agh.cs.to.cinemamak.event.NewMovieAddedEvent;
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

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
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
        String durationStr = this.textFieldDuration.getCharacters().toString();
        String genreName = choiceBoxGenre.getValue();
        String image = this.textFieldImage.getCharacters().toString();
        LocalDate date =  datePicker.getValue();

        if (validate(title, director, description, genreName, image, durationStr, date)) {
            movieService.addMovie(new Movie(title, director, description, Integer.parseInt(durationStr), movieService.getGenreByName(genreName).get(), Date.valueOf(date), image));

            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Information");
            dialog.setHeaderText("New movie added successfully");
            dialog.show();
            dialog.setOnCloseRequest(event -> {
                applicationEventPublisher.publishEvent(new NewMovieAddedEvent(this));
                stage.close();
            });
        }
    }

    public boolean validate(String title, String director, String description, String genreName, String image, String durationStr, LocalDate date) {
        if (title.isEmpty() || director.isEmpty() || description.isEmpty() || genreName.isEmpty() || durationStr.isEmpty() || image.isEmpty() || date == null) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Error");
            dialog.setHeaderText("Error occurred while adding a new movie");
            dialog.setContentText("All fields need to be filled!");
            dialog.show();

            return false;
        }

        try {
            Integer.parseInt(durationStr);
        } catch (NumberFormatException e) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Error");
            dialog.setHeaderText("Error occurred while adding a new movie");
            dialog.setContentText("Duration should be a positive integer!");
            dialog.show();

            return false;
        }

        Optional<Genre> genre =  movieService.getGenreByName(genreName);
        if (genre.isEmpty()) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Error");
            dialog.setHeaderText("Error occurred while adding a new movie!");
            dialog.setContentText("Genre does not exist!");
            dialog.show();

            return false;
        }

        return true;
    }

    public void setStage(Stage s) {
        this.stage = s;
    }
}
