package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

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

    public PerformanceFormViewController(){

    }

    public void initialize(){

    }

    public void onActionAdd(){

    }

    public void onActionCancel(){

    }


}
