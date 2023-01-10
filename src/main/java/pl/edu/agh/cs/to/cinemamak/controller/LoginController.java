package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.config.DialogManager;
import pl.edu.agh.cs.to.cinemamak.helpers.ResizeHelper;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

@Component
@FxmlView("login-view.fxml")
public class LoginController {
    @FXML
    private BorderPane borderPane;
    private double x = 0;
    private double y = 0;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldPassword;
    private final UserService userService;
    private final SessionService sessionService;
    private final FxWeaver fxWeaver;
    private final DialogManager dialogManager;
    private Stage stage;

    public LoginController(UserService userService, SessionService sessionService, FxWeaver fxWeaver, DialogManager dialogManager) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
        this.dialogManager = dialogManager;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onButtonLogin() {
        String email = this.textFieldEmail.getCharacters().toString();
        String password = this.textFieldPassword.getCharacters().toString();

        if (userService.authenticate(email, password)) {
            this.dialogManager.showInformation(stage, "Logged in successfully!", "Enjoy!", event -> {
                sessionService.setCurrentUser(userService.getUserByEmail(email).get());
                Scene scene = new Scene(fxWeaver.loadView(HomeController.class));
                stage.setScene(scene);
                ResizeHelper.addResizeListener(stage, scene.getWidth(), scene.getHeight(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight() );
            });
        } else {
            this.dialogManager.showError(stage, "Error occurred while logging in!", "Invalid credentials!");
        }
    }

    @FXML
    private void onButtonRegister() {
        Scene registerScene = new Scene(fxWeaver.loadView(RegisterController.class));
        stage.setScene(registerScene);
    }

    @FXML
    private void onButtonExit() {
        Platform.exit();
    }

    @FXML
    public void onBorderPaneDragged(MouseEvent event) {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setY(event.getScreenY() - y);
        stage.setX(event.getScreenX() - x);
    }

    @FXML
    public void onBorderPanePressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }
}