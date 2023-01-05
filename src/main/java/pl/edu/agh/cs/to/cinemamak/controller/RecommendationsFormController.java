package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.event.MovieSelectedEvent;
import pl.edu.agh.cs.to.cinemamak.event.TableRecommendationsChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Component
@FxmlView("recommendations-form-view.fxml")
public class RecommendationsFormController implements ApplicationListener<MovieSelectedEvent> {
    @FXML
    public ChoiceBox<String> movieChoiceBox;
    @FXML
    public DatePicker dateFromPicker;
    @FXML
    public DatePicker dateToPicker;
    public TextField textFieldMovie;
    public Button buttonSearch;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final MovieService movieService;
    private final RecommendationService recommendationService;
    private final FxWeaver fxWeaver;
    private Stage stage;
    private Optional<Recommendation> recommendation = Optional.empty();
    private Optional<Movie> selectedMovie = Optional.empty();
    public RecommendationsFormController(MovieService movieService,
                                     RecommendationService recommendationService,
                                         FxWeaver fxWeaver){
        this.movieService = movieService;
        this.recommendationService = recommendationService;
        this.fxWeaver = fxWeaver;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

//    public void initialize(){
//        this.movieService.getMovies().ifPresent(list -> list.forEach(movie ->
//                this.movieChoiceBox.getItems().add(movie.getId()+" "+movie.getTitle())));
//    }

    public void onActionAdd(){
        if(this.selectedMovie.isEmpty()){
            showErrorDialog("Error occurred while editing a recommendation",
                    "Movie must be chosen.");
            return;
        }
//        String title = this.movieChoiceBox.getValue();
        String title = this.selectedMovie.get().getTitle();
        Long movieId = this.selectedMovie.get().getId();
        LocalDate dateFrom = this.dateFromPicker.getValue();
        LocalDate dateTo = this.dateToPicker.getValue();

        if(dateTo != null && dateFrom != null && title != null){
//            Optional<Movie> movie = movieService.getMovieById(Long.parseLong(title.split("\\s")[0]));
            Optional<Movie> movie = movieService.getMovieById(movieId);
            if(dateTo.isBefore(dateFrom)){
                showErrorDialog("Error occurred while adding a new recommendation",
                        "Begin date must be before end date.");
                return;
            }

            LocalTime time = LocalTime.of(0, 0, 0);
            LocalDateTime localDateTimeFrom = LocalDateTime.of(dateFrom, time);
            LocalDateTime localDateTimeTo = LocalDateTime.of(dateTo, time);


            if(movie.isPresent()){
                this.recommendationService.addRecommendation(new Recommendation(movie.get(), localDateTimeFrom, localDateTimeTo));
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                dialog.setTitle("Information");
                dialog.setHeaderText("New recommendation added successfully");
                dialog.show();
                dialog.setOnCloseRequest(event -> {
                    applicationEventPublisher.publishEvent(new TableRecommendationsChangeEvent(this));
                    stage.close();
                });
            }
            else{
                showErrorDialog("Error occurred while adding a new recommendation",
                        "All fields need to be filled!");
            }
        }
        else{
            showErrorDialog("Error occurred while adding a new recommendation",
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
        stage.close();
    }

    public void onActionSearch(ActionEvent actionEvent) {
        Stage stageMovieSearch = new Stage();
        this.selectedMovie = Optional.of(new Movie());
        fxWeaver.loadController(MovieSearchController.class).setStage(stageMovieSearch);
        fxWeaver.loadController(MovieSearchController.class).setSelectedMovie(this.selectedMovie.get());

        Scene scene = new Scene(fxWeaver.loadView(MovieSearchController.class));
        stageMovieSearch.setScene(scene);
        stageMovieSearch.setTitle("Movie search");
        stageMovieSearch.initModality(Modality.WINDOW_MODAL);
        stageMovieSearch.setAlwaysOnTop(true);
        stageMovieSearch.initOwner(stage);
        stageMovieSearch.show();
    }

    @Override
    public void onApplicationEvent(MovieSelectedEvent event) {
        if(this.selectedMovie.isEmpty()) return;
        this.textFieldMovie.setText(this.selectedMovie.get().getTitle());
    }

}