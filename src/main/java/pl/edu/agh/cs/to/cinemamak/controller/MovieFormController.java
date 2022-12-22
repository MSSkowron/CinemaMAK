package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;

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
    }
}
