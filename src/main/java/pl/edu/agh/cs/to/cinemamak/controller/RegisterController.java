package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.User;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

import java.sql.SQLException;
import java.util.Arrays;

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

    @FXML
    private Button buttonLogin;

    private UserService userService;
    private Stage stage;
    private final FxWeaver fxWeaver;
    public RegisterController(UserService userService, FxWeaver fxWeaver) {
        this.userService = userService;
        this.fxWeaver = fxWeaver;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onButtonExitClick(){
        Platform.exit();
    }

    @FXML
    private void onButtonRegisterClick(){
        if(textFieldFirstName.getText().isEmpty() || textFieldLastName.getText().isEmpty() || textFieldEmail.getText().isEmpty() || textFieldPassword.getText().isEmpty()){
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Error");
            dialog.setHeaderText("Error occurred while creating an account!");
            dialog.setContentText("All fields need to be filled!");
            dialog.show();

            return;
        }

        if(!EmailValidator.getInstance().isValid(textFieldEmail.getText())){
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Error");
            dialog.setHeaderText("Error occurred while creating an account!");
            dialog.setContentText("Email is not valid!");
            dialog.show();
            return;
        }

        User userDto = new User();
        userDto.setFirstName(textFieldFirstName.getText());
        userDto.setLastName(textFieldLastName.getText());
        userDto.setEmailAddress(textFieldEmail.getText());
        userDto.setPassword(textFieldPassword.getText());

        try {
            userService.addUser(userDto);
        } catch (Exception|Error e) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Error");
            dialog.setHeaderText("Error occurred while creating an account!");
            dialog.setContentText(getCauseMessage(e));
            dialog.show();
            return;
        }

        textFieldFirstName.setText("");
        textFieldLastName.setText("");
        textFieldEmail.setText("");
        textFieldPassword.setText("");

        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Registration success");
        dialog.setHeaderText("Account created successfully!");
        dialog.setContentText("You can log in now!");
        dialog.show();
        dialog.setOnCloseRequest(event -> {
            Scene scene = new Scene(fxWeaver.loadView(LoginController.class), 616, 433);
            stage.setScene(scene);
        });
    }

    @FXML
    private void onButtonLogin() {
        Scene scene = new Scene(fxWeaver.loadView(LoginController.class), 616, 433);
        stage.setScene(scene);
    }

    private String getCauseMessage(Throwable t){
        Throwable cause = t;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause.getLocalizedMessage();
    }
}
