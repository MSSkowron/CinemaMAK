package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import pl.edu.agh.cs.to.cinemamak.config.DialogManager;
import pl.edu.agh.cs.to.cinemamak.event.TableMovieChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.Genre;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;

import java.time.LocalDate;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Component
@FxmlView("movie-form-view.fxml")
public class MovieFormController {
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
    public TextField textFieldImageURL;
    @FXML
    public Button buttonSubmit;
    private Stage stage;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private final DialogManager dialogManager;
    private final MovieService movieService;

    public MovieFormController(MovieService movieService, DialogManager dialogManager) {
        this.movieService = movieService;
        this.dialogManager = dialogManager;
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

        movieService.getGenres().ifPresent(listM -> listM.forEach(genre -> this.choiceBoxGenre.getItems().add(genre.getGenreName())));
    }

    @FXML
    public void onButtonSubmit(MouseEvent actionEvent) {
        String title = this.textFieldTitle.getCharacters().toString();
        String director = this.textFieldDirector.getCharacters().toString();
        String description = this.textAreaDescription.getText();
        String durationStr = this.textFieldDuration.getCharacters().toString();
        String genreName = choiceBoxGenre.getValue();
        String imageURL = this.textFieldImageURL.getCharacters().toString();
        LocalDate date =  datePicker.getValue();

        if (validate(title, director, description, genreName, imageURL, durationStr, date)) {
            LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(0,0));
            movieService.addMovie(new Movie(title, director, description, Integer.parseInt(durationStr), movieService.getGenreByName(genreName).get(), dateTime, imageURL));

            this.dialogManager.showInformation(stage, "New movie added successfully", "", event -> {
                applicationEventPublisher.publishEvent(new TableMovieChangeEvent(this));
                stage.close();
            });

        }
    }

    public boolean validate(String title, String director, String description, String genreName, String imageURL, String durationStr, LocalDate date) {
        if (title.isEmpty() || director.isEmpty() || description.isEmpty() || genreName.isEmpty() || durationStr.isEmpty() || imageURL.isEmpty() || date == null) {
            this.dialogManager.showError(stage, "Error occurred while adding a new movie", "All fields need to be filled!");
            return false;
        }

        try {
            Integer.parseInt(durationStr);
        } catch (NumberFormatException e) {
            this.dialogManager.showError(stage, "Error occurred while adding a new movie", "Duration should be a positive integer!");
            return false;
        }

        Optional<Genre> genre =  movieService.getGenreByName(genreName);
        if (genre.isEmpty()) {
            this.dialogManager.showError(stage,
                    "Error occurred while adding a new movie",
                    "Genre does not exist!");
            return false;
        }

        if (!isValidURL(imageURL)) {
            this.dialogManager.showError(stage,
                    "Error occurred while adding a new movie",
                    "Image is not valid!");
            return false;
        }

        try {
            if (ImageIO.read(new URL(imageURL)) == null) {
                this.dialogManager.showError(stage,
                        "Error occurred while adding a new movie",
                        "Image is not valid!");
                return false;
            }
        } catch (IOException e) {
            this.dialogManager.showError(stage,
                    "Error occurred while adding a new movie",
                    "Image is not valid!");
            return false;
        }

        return true;
   }

    boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    public void setStage(Stage s) {
        this.stage = s;
    }
}
