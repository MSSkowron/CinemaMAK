package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;

import javax.naming.Binding;

@Component
@FxmlView("home-view.fxml")
public class HomeController {
    @FXML
    private Label helloLabel;
    private final SessionService sessionService;
    private final FxWeaver fxWeaver;
    private Stage stage;

    public HomeController(SessionService sessionService, FxWeaver fxWeaver) {
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        helloLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            if (sessionService.getCurrentUser().isEmpty()) {
                return "";
            }
            var user = sessionService.getCurrentUser().get();
            return "Hello, %s %s!".formatted(user.getFirstName(), user.getLastName());
        }, sessionService.getCurrentUserProperty()));
    }

    public void onLogOutClick() {
        sessionService.endSession();
        var scene = new Scene(fxWeaver.loadView(LoginController.class));
        stage.setScene(scene);
    }
}
