package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.service.IEntityService;
import pl.edu.agh.cs.to.cinemamak.service.RecommendationService;

import java.util.function.Predicate;

@Component
public class ExtractedTableController<EntityType> {

    @FXML
    protected TableView<EntityType> table;

    protected final IEntityService<EntityType> entityService;

    protected Stage stage;

    protected ExtractedTableController(IEntityService<EntityType> entityService) {
        this.entityService = entityService;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ObservableList<EntityType> getEntities(){
        ObservableList<EntityType> recommendationObservableList = FXCollections.observableArrayList();
        this.entityService.getEntities().ifPresent(recommendationObservableList::addAll);
        return recommendationObservableList;
    }

    public void setEntities(Predicate<EntityType> predicate){
        FilteredList<EntityType> filteredList = new FilteredList<>(getEntities(), predicate);
        SortedList<EntityType> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(this.table.comparatorProperty());
        this.table.setItems(sortedList);
    }

}
