package pl.edu.agh.cs.to.cinemamak.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.commons.validator.routines.EmailValidator;

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

    @FXML
    private void onButtonExitClick(){
        Platform.exit();
    }

    @FXML
    private void onButtonRegisterClick(){
        String firstName = this.textFieldFirstName.getCharacters().toString();
        String lastName = this.textFieldLastName.getCharacters().toString();
        String email = this.textFieldEmail.getCharacters().toString();
        String password = this.textFieldPassword.getCharacters().toString();

        if(EmailValidator.getInstance().isValid(email)){
            System.out.println("Email is valid");
        }
    }

}
