package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import pl.edu.agh.cs.to.cinemamak.event.TablePerformanceChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.model.Room;
import pl.edu.agh.cs.to.cinemamak.model.User;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;
import pl.edu.agh.cs.to.cinemamak.service.PerformanceService;
import pl.edu.agh.cs.to.cinemamak.service.RoomService;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@FxmlView("performance-edit-view.fxml")
public class PerformanceEditController implements ApplicationListener<MovieSelectedEvent> {

    public TextField textFieldMovie;
    public Button buttonSearch;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<String> roomChoiceBox;
    @FXML
    private ChoiceBox<String> supervisorChoiceBox;
    @FXML
    private Spinner<String> hourSpinner;
    @FXML
    private Button applyButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField priceTextField;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final MovieService movieService;
    private final RoomService roomService;
    private final UserService userService;
    private final PerformanceService performanceService;
    private final FxWeaver fxWeaver;
    private Stage stage;

    private Optional<Performance> performance = Optional.empty();
    private Optional<Movie> selectedMovie = Optional.empty();

    public PerformanceEditController(MovieService movieService,
                                     RoomService roomService,
                                     UserService userService,
                                     PerformanceService performanceService,
                                     FxWeaver fxWeaver){
        this.userService = userService;
        this.roomService = roomService;
        this.movieService = movieService;
        this.performanceService = performanceService;
        this.fxWeaver = fxWeaver;
    }

    public PerformanceEditController setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public PerformanceEditController setPerformance(Performance perf) {
        this.performance = Optional.of(perf);
        return this;
    }

    public void initialize(){

        if(performance.isPresent()) {
            this.setFields();
        }
        this.userService.getUsers().ifPresent(list -> list.forEach(user ->
                this.supervisorChoiceBox.getItems().add(user.getId()+" "+user.getFirstName()+ " " + user.getLastName())));

        this.roomService.getRooms().ifPresent(list -> list.forEach(room ->
                this.roomChoiceBox.getItems().add(room.getId()+" "+room.getName())));

        List<String> hours = new ArrayList<>();
        for(int i = 8; i<24; i++){
            hours.add(i+":00");
            hours.add(i+":30");
        }

        SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory<String>() {
            int ptr = 0;
            @Override
            public void decrement(int steps) {
                if(ptr - steps != -1) ptr -= steps;
                setValue(hours.get(ptr));
            }

            @Override
            public void increment(int steps) {
                if(ptr+ steps != hours.size()) ptr += steps;
                setValue(hours.get(ptr));
            }
        };

        this.hourSpinner.setValueFactory(valueFactory);
    }

    public void setFields(){
        if(this.performance.isPresent()) {
            Performance perf = this.performance.get();

            this.supervisorChoiceBox.setValue("value2");
            String hourStr;
            if (perf.getDate().getMinute() == 0) {
                hourStr = perf.getDate().getHour() + ":00";
            } else {
                hourStr = perf.getDate().getHour() + ":" + perf.getDate().getMinute();
            }
            this.hourSpinner.setPromptText(hourStr);
            this.priceTextField.setText(perf.getPrice().toString());
            this.datePicker.setValue(perf.getDate().toLocalDate());
            this.roomChoiceBox.setValue(perf.getRoom().getId() + " " + perf.getRoom().getName());
            this.supervisorChoiceBox.setValue(perf.getUser().getId() + " " + perf.getUser().getFirstName() + " " + perf.getUser().getLastName());
            this.textFieldMovie.setText(perf.getMovie().getTitle());
            this.selectedMovie = Optional.of(perf.getMovie());
        }
    }

    public void onActionApply(){
        if(this.selectedMovie.isEmpty()){
            showErrorDialog("Error occurred while editing a recommendation",
                    "Movie must be chosen.");
            return;
        }
        String title = this.selectedMovie.get().getTitle();
        Long movieId = this.selectedMovie.get().getId();
        String name_room = this.roomChoiceBox.getValue();
        String supervisor = this.supervisorChoiceBox.getValue();
        Double price = null;

        try {
            price = Double.parseDouble(
                    this.priceTextField.getCharacters().toString());
        } catch (NullPointerException nullPointerException){
            showErrorDialog("Error occurred while editing performance",
                    "All fields need to be filled!");
            return;
        } catch(NumberFormatException numberFormatException){
            showErrorDialog("Error occurred while editing performance",
                    "Price need to be in format: integer.integer or integer.");
            return;
        }

        LocalDate date = this.datePicker.getValue();
        String hour_str = this.hourSpinner.getValue();

        if(hour_str != null && date != null && title != null && name_room  != null && supervisor  != null ){
            int hour1 = Integer.parseInt(hour_str.split(":")[0]);
            int minute1 = Integer.parseInt(hour_str.split(":")[1]);
            LocalTime time = LocalTime.of(hour1, minute1, 0);
//            Optional<Movie> movie = movieService.getMovieById(Long.parseLong(title.split("\\s")[0]));
            Optional<Movie> movie = movieService.getMovieById(movieId);
            Optional<Room> room = roomService.getRoomById(Long.parseLong(name_room.split("\\s")[0]));
            Optional<User> user = userService.getUserById(Long.parseLong(supervisor.split("\\s")[0]));

            LocalDateTime localDateTime1 = LocalDateTime.of(date, time);

            if(movie.isPresent() && room.isPresent() && user.isPresent() && this.performance.isPresent()){
                this.performance.get().setUser(user.get());
                this.performance.get().setMovie(movie.get());
                this.performance.get().setRoom(room.get());
                this.performance.get().setPrice(BigDecimal.valueOf(price));
                this.performance.get().setDate(localDateTime1);

                this.performanceService.addPerformance(this.performance.get());

                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                dialog.setTitle("Information");
                dialog.setHeaderText("Performance edited successfully");
                dialog.show();
                dialog.setOnCloseRequest(event -> {
                    applicationEventPublisher.publishEvent(new TablePerformanceChangeEvent(this));
                    stage.close();
                });

            }
            else{
                showErrorDialog("Error occurred while editing performance",
                        "All fields need to be filled!");
            }
        }
        else{
            showErrorDialog("Error occurred while editing performance",
                    "All fields need to be filled! (look at hour field)");
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

    public void onActionCancel(){
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
