package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

import java.io.IOException;

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

    private UserService userService;
    private final FxWeaver fxWeaver;
    private Stage stage;

    public LoginController(UserService userService, FxWeaver fxWeaver) {
        this.userService = userService;
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

        String email = this.textFieldEmail.getCharacters().toString();
        String password = this.textFieldPassword.getCharacters().toString();

        if(EmailValidator.getInstance().isValid(email)){
            System.out.println("Email is valid");
        }
    }

    @FXML
    private void onButtonRegister(){
        Scene scene = new Scene(fxWeaver.loadView(RegisterController.class), 616, 433);
        stage.setScene(scene);
    }

}