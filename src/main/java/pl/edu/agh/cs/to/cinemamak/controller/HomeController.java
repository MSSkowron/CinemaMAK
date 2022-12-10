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
import pl.edu.agh.cs.to.cinemamak.model.RoleName;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;

import javax.naming.Binding;

@Component
@FxmlView("home-view.fxml")
public class HomeController {
    @FXML
    private Label helloLabel;

    @FXML
    private Button controlPanelButton;

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

        if(sessionService.getCurrentUser().isPresent())
            if(!sessionService.getCurrentUser().get().getRole().getRoleName().equals(RoleName.Admin))
                controlPanelButton.setOpacity(0);
    }

    public void onLogOutClick() {
        sessionService.endSession();
        var scene = new Scene(fxWeaver.loadView(LoginController.class));
        stage.setScene(scene);
    }

    @FXML
    public void onControlClick(){
        Scene scene = new Scene(fxWeaver.loadView(AdminController.class));
        stage.setScene(scene);
    }

}
