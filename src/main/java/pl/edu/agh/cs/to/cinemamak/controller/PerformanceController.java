package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("performance-view.fxml")
public class PerformanceController {

    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;

    PerformanceController(){

    }

}
