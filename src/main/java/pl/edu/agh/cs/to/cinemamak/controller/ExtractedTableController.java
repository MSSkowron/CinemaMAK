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
import pl.edu.agh.cs.to.cinemamak.config.DialogManager;
import pl.edu.agh.cs.to.cinemamak.event.TableRecommendationsChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.Genre;
import pl.edu.agh.cs.to.cinemamak.model.ITableEntityWithMovie;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.service.ITableEntityService;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExtractedTableController<EntityType extends ITableEntityWithMovie> extends MovieSearchBarController<EntityType> {

    @FXML
    protected TableView<EntityType> table;

    protected ITableEntityService<EntityType> entityService;

    private final MovieService movieService;


    protected Stage stage;

    protected ExtractedTableController(MovieService movieService) {
        this.movieService = movieService;
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

    protected void setEntities(Predicate<EntityType> predicate, ObservableList<EntityType> observableList){
        FilteredList<EntityType> filteredList = new FilteredList<>(observableList, predicate);
        SortedList<EntityType> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(this.table.comparatorProperty());
        this.table.setItems(sortedList);
    }

    protected void deleteEntity(){
        EntityType entity = this.table.getSelectionModel().getSelectedItem();
        this.entityService.deleteEntityById(entity.getId());
    }

    protected void resetTable(){
        setEntities(e -> false, FXCollections.observableArrayList());
        this.table.refresh();
    }

    private ObservableList<EntityType> getEntitiesByMovie(String title, String director, int year, Genre genre){
        ObservableList<EntityType> entityObservableList = FXCollections.observableArrayList();
        Optional<List<Movie>> movieList = movieService.getListOfEntitiesWithParticularMovie(title, director, year, genre);
        if(movieList.isPresent()){

            for (Movie m: movieList.get()) {
                Optional<List<EntityType>> entityList = entityService.getEntitiesByMovieId(m.getId());
                if(entityList.isPresent() && !entityList.get().isEmpty()){
                    entityObservableList.addAll(entityList.get());
                }
            }
        }
        return entityObservableList;
    }

    protected void searchByMovie(){

        String title = titleTextField.getText();
        String director = directorTextField.getText();

        String yearText = yearTextField.getText();
        int year = 0;
        if(!yearText.equals("")) year = Integer.parseInt(yearText);

        String genreName = genreChoiceBox.getValue();
        Genre genre = null;
        if(genreName != null && !genreName.equals("")){
            Optional<Genre> genreOptional = movieService.getGenreByName(genreName);
            if(genreOptional.isPresent()){
                genre = genreOptional.get();
            }
        }

        setEntities(getMoviePredicate(), getEntitiesByMovie(title, director, year, genre));

    }

}
