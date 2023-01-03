package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.event.TableRecommendationsChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;
import pl.edu.agh.cs.to.cinemamak.service.RecommendationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Component
@FxmlView("recommendations-edit-view.fxml")
public class RecommendationsEditController {
    @FXML
    public ChoiceBox<String> movieChoiceBox;
    @FXML
    public DatePicker dateFromPicker;
    @FXML
    public DatePicker dateToPicker;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final MovieService movieService;
    private final RecommendationService recommendationService;
    private Stage stage;
    private Optional<Recommendation> recommendation = Optional.empty();

    public RecommendationsEditController(MovieService movieService,
                                         RecommendationService recommendationService){
        this.movieService = movieService;
        this.recommendationService = recommendationService;
    }

    public RecommendationsEditController setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public RecommendationsEditController setRecommendation(Recommendation recom) {
        this.recommendation = Optional.of(recom);
        return this;
    }

    public void initialize(){

        if(recommendation.isPresent()){
            this.setFields();
        }

        this.movieService.getMovies().ifPresent(list -> list.forEach(movie ->
                this.movieChoiceBox.getItems().add(movie.getId()+" "+movie.getTitle())));
    }

    public void setFields(){
        if(this.recommendation.isPresent()) {
            Recommendation recom = this.recommendation.get();
            this.dateFromPicker.setValue(recom.getDateFrom().toLocalDate());
            this.dateToPicker.setValue(recom.getDateTo().toLocalDate());
            this.movieChoiceBox.setValue(recom.getMovie().getId() + " " + recom.getMovie().getTitle());
        }
    }

    public void onActionApply(ActionEvent actionEvent) {
        String title = this.movieChoiceBox.getValue();
        LocalDate dateFrom = this.dateFromPicker.getValue();
        LocalDate dateTo = this.dateToPicker.getValue();

        if(dateTo != null && dateFrom != null && title != null){
            Optional<Movie> movie = movieService.getMovieById(Long.parseLong(title.split("\\s")[0]));

            if(dateTo.isBefore(dateFrom)){
                showErrorDialog("Error occurred while editing a recommendation",
                        "Begin date must be before end date.");
                return;
            }

            LocalTime time = LocalTime.of(0, 0, 0);
            LocalDateTime localDateTimeFrom = LocalDateTime.of(dateFrom, time);
            LocalDateTime localDateTimeTo = LocalDateTime.of(dateTo, time);


            if(movie.isPresent() && this.recommendation.isPresent()){

                this.recommendation.get().setMovie(movie.get());
                this.recommendation.get().setDateFrom(localDateTimeFrom);
                this.recommendation.get().setDateTo(localDateTimeTo);
                this.recommendationService.addRecommendation(this.recommendation.get());

                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                dialog.setTitle("Information");
                dialog.setHeaderText("Recommendation edited successfully");
                dialog.show();
                dialog.setOnCloseRequest(event -> {
                    applicationEventPublisher.publishEvent(new TableRecommendationsChangeEvent(this));
                    stage.close();
                });
            }
            else{
                showErrorDialog("Error occurred while editing a recommendation",
                        "All fields need to be filled!");
            }
        }
        else{
            showErrorDialog("Error occurred while editing a recommendation",
                    "All fields need to be filled!");
        }
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

    public void onActionCancel(ActionEvent actionEvent) {
        this.stage.close();
    }
}
