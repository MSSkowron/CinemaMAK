package pl.edu.agh.cs.to.cinemamak.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.models.User;
import pl.edu.agh.cs.to.cinemamak.services.UserService;

@Component
@FxmlView("register-view.fxml")
public class RegisterController {

    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldLastName;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonRegister;

    private UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private void onButtonExitClick(){
        Platform.exit();
    }

    @FXML
    private void onButtonRegisterClick(){
        if(textFieldFirstName.getText().isEmpty() || textFieldLastName.getText().isEmpty() || textFieldEmail.getText().isEmpty() || textFieldPassword.getText().isEmpty()){
            System.out.println("All fields need to be filled");
            return;
        }

        if(!EmailValidator.getInstance().isValid(textFieldEmail.getText())){
            System.out.println("Email is not valid");
            return;
        }

        userService.addUser(new User(textFieldFirstName.getText(), textFieldLastName.getText(), textFieldEmail.getText(), textFieldPassword.getText()));
    }
}
