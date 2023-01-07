package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.event.TableRecommendationsChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.ITableEntityWithMovie;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.service.ITableEntityService;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExtractedTableController<EntityType extends ITableEntityWithMovie> extends MovieSearchBarController {

    @FXML
    protected TableView<EntityType> table;

    protected ITableEntityService<EntityType> entityService;

    protected Stage stage;

    protected ExtractedTableController() {
    }
    protected void setService(ITableEntityService<EntityType> entityService){
        this.entityService = entityService;
    }

    protected void initialize(){
        super.initialize();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    protected ObservableList<EntityType> getEntities(){
        ObservableList<EntityType> entityObservableList = FXCollections.observableArrayList();
        this.entityService.getEntities().ifPresent(entityObservableList::addAll);
        return entityObservableList;
    }

    protected void setEntities(Predicate<EntityType> predicate){
        FilteredList<EntityType> filteredList = new FilteredList<>(getEntities(), predicate);
        SortedList<EntityType> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(this.table.comparatorProperty());
        this.table.setItems(sortedList);
    }

    protected void deleteEntity(){
        EntityType entity = this.table.getSelectionModel().getSelectedItem();
        this.entityService.deleteEntityById(entity.getId());
    }

    protected void searchAccordinglyToMovies(){

        String title = titleTextField.getText();
        String director = directorTextField.getText();
        String year = yearTextField.getText();
        String genre = genreChoiceBox.getValue();

        setEntities(new Predicate<EntityType>() {
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
        });
    }

    protected void resetTable(){
        setEntities(e -> true);
        this.table.refresh();
    }

}
