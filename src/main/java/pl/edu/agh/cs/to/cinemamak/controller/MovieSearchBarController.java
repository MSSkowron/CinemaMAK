package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class MovieSearchBarController {
    
    @FXML
    protected TextField titleTextField;
    @FXML
    protected TextField directorTextField;
    @FXML
    protected TextField yearTextField;
    @FXML
    protected ChoiceBox<String> genreChoiceBox;

    protected MovieSearchBarController(){

    }

    protected void initialize(){
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        this.yearTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
    }

    protected void cleanFields(){
        this.genreChoiceBox.setValue("");
        this.directorTextField.setText("");
        this.titleTextField.setText("");
        this.yearTextField.setText("");
    }

}
