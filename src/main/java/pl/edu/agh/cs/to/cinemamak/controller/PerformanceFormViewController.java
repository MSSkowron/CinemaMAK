package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.event.TableChangePerformanceEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.model.Room;
import pl.edu.agh.cs.to.cinemamak.model.User;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;
import pl.edu.agh.cs.to.cinemamak.service.PerformanceService;
import pl.edu.agh.cs.to.cinemamak.service.RoomService;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@FxmlView("performance-form-view.fxml")
public class PerformanceFormViewController {

    @FXML
    private ChoiceBox<String> movieChoiceBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<String> roomChoiceBox;
    @FXML
    private ChoiceBox<String> supervisorChoiceBox;
    @FXML
    private Spinner<String> hourSpinner;
    @FXML
    private Button addButton;
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
    private Stage stage;

    public PerformanceFormViewController(MovieService movieService,
                                         RoomService roomService,
                                         UserService userService,
                                         PerformanceService performanceService){
        this.userService = userService;
        this.roomService = roomService;
        this.movieService = movieService;
        this.performanceService = performanceService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(){

        this.userService.getUsers().ifPresent(list -> list.forEach(user ->
                this.supervisorChoiceBox.getItems().add(user.getId()+" "+user.getFirstName()+ " " + user.getLastName())));

        this.roomService.getRooms().ifPresent(list -> list.forEach(room ->
                this.roomChoiceBox.getItems().add(room.getId()+" "+room.getName())));

        this.movieService.getMovies().ifPresent(list -> list.forEach(movie ->
                this.movieChoiceBox.getItems().add(movie.getId()+" "+movie.getTitle())));

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

    public void onActionAdd(){
        String title = this.movieChoiceBox.getValue();
        String name_room = this.roomChoiceBox.getValue();
        String supervisor = this.supervisorChoiceBox.getValue();
        Double price = null;

        try {
            price = Double.parseDouble(
                    this.priceTextField.getCharacters().toString());
        } catch (NullPointerException nullPointerException){
            showErrorDialog("Error occurred while adding a new performance",
                    "All fields need to be filled!");
            return;
        } catch(NumberFormatException numberFormatException){
            showErrorDialog("Error occurred while adding a new performance",
                    "Price need to be in format: integer.integer or integer.");
            return;
        }

        LocalDate date = this.datePicker.getValue();
        String hour_str = this.hourSpinner.getValue();
        int hour1 = Integer.parseInt(hour_str.split(":")[0]);
        int minute1 = Integer.parseInt(hour_str.split(":")[1]);
        LocalTime time = LocalTime.of(hour1, minute1, 0);

        if(time != null && date != null && title != null && name_room  != null && supervisor  != null ){
            Optional<Movie> movie = movieService.getMovieById(Long.parseLong(title.split("\\s")[0]));
            Optional<Room> room = roomService.getRoomById(Long.parseLong(name_room.split("\\s")[0]));
            Optional<User> user = userService.getUserById(Long.parseLong(supervisor.split("\\s")[0]));

            LocalDateTime localDateTime1 = LocalDateTime.of(date, time);

            if(movie.isPresent() && room.isPresent() && user.isPresent()){
                this.performanceService.addPerformance(
                        new Performance(movie.get(),
                                        room.get(),
                                        localDateTime1,
                                        BigDecimal.valueOf(price),
                                        user.get()));
                applicationEventPublisher.publishEvent(new TableChangePerformanceEvent(this));
                stage.close();
            }
            else{
                showErrorDialog("Error occurred while adding a new performance",
                        "All fields need to be filled!");
            }
        }
        else{
            showErrorDialog("Error occurred while adding a new performance",
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

    public void onActionCancel(){
        stage.close();
    }


}
