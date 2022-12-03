package pl.edu.agh.cs.to.cinemamak.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.JavaFxApplication;
import pl.edu.agh.cs.to.cinemamak.services.UserService;

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

    public LoginController(UserService userService) {
        this.userService = userService;
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 616, 433);
//            stage.setTitle("Cinema Application");
//            stage.setScene(scene);
//            stage.show();

        } catch( IOException exception){
            exception.getStackTrace();
        }
    }

}