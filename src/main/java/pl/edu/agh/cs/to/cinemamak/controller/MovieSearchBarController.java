package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.ITableEntityWithMovie;
import pl.edu.agh.cs.to.cinemamak.model.Movie;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MovieSearchBarController<EntityType extends ITableEntityWithMovie> {

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

    protected void setFilterListeners(FilteredList<EntityType> filteredList){
        this.titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(this.getMoviePredicate());
        });
        this.directorTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(this.getMoviePredicate());
        });
        this.yearTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(this.getMoviePredicate());
        });
        this.genreChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(this.getMoviePredicate());
        });
    }

    protected Predicate<EntityType> getMoviePredicate(){
        return new Predicate<EntityType>() {
            final String title = titleTextField.getText();
            final String director = directorTextField.getText();
            final String year = yearTextField.getText();
            final String genre = genreChoiceBox.getValue();
            @Override
            public boolean test(EntityType entity) {
                Movie movie = entity.getMovie();
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

                    if (!Integer.valueOf(movie.getDate().getYear()).equals(Integer.valueOf(year))) {
                        return false;
                    }
                }
                if(genre != null && !genre.equals("")){

                    if(!movie.getGenre().getGenreName().equals(genre)){
                        return false;
                    }
                }
                return true;
            }
        };
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
