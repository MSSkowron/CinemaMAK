package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

@Component
@FxmlView("login-view.fxml")
public class LoginController {

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonLogin;

    @FXML
    private Button buttonRegister;

    private final UserService userService;
    private final SessionService sessionService;
    private final FxWeaver fxWeaver;
    private Stage stage;

    public LoginController(UserService userService, SessionService sessionService, FxWeaver fxWeaver) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onButtonExit(){
        Platform.exit();
    }

    @FXML
    private void onButtonLogin(){

        String username = this.textFieldEmail.getCharacters().toString();
        String password = this.textFieldPassword.getCharacters().toString();

        if (userService.authenticate(username, password)){
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Login success");
            dialog.setHeaderText("Logged in successfully!");
            dialog.setContentText("Enjoy!");
            dialog.show();
            dialog.setOnCloseRequest(event -> {
                sessionService.setCurrentUser(userService.getUserByUsername(username).get());
                Scene scene = new Scene(fxWeaver.loadView(HomeController.class), 616, 433);
                stage.setScene(scene);
            });
        } else {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Error");
            dialog.setHeaderText("Error occurred while logging in!");
            dialog.setContentText("Invalid credentials!");
            dialog.show();
        }
    }

    @FXML
    private void onButtonRegister(){
        Scene scene = new Scene(fxWeaver.loadView(RegisterController.class), 616, 433);
        stage.setScene(scene);
    }
}