package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;
import pl.edu.agh.cs.to.cinemamak.service.RoomService;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

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
    private Button addButton;
    @FXML
    private Button cancelButton;

    private final MovieService movieService;
    private final RoomService roomService;
    private final UserService userService;
    private Stage stage;

    public PerformanceFormViewController(MovieService movieService,
                                         RoomService roomService,
                                         UserService userService){
        this.userService = userService;
        this.roomService = roomService;
        this.movieService = movieService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(){

    }

    public void onActionAdd(){

    }

    public void onActionCancel(){

    }


}
