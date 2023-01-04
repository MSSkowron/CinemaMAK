package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.User;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

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
    private final UserService userService;
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
    private void onButtonRegister() {
        if(textFieldFirstName.getText().isEmpty() || textFieldLastName.getText().isEmpty() || textFieldEmail.getText().isEmpty() || textFieldPassword.getText().isEmpty()) {
            showErrorDialog("All fields need to be filled!");
            return;
        }

        if(!EmailValidator.getInstance().isValid(textFieldEmail.getText())) {
            showErrorDialog("Email is not valid!");
            return;
        }

        User newUser = new User(textFieldFirstName.getText(), textFieldLastName.getText(), textFieldEmail.getText(), textFieldPassword.getText());

        try {
            userService.addUser(newUser);
        } catch (Exception|Error e) {
            showErrorDialog(getCauseMessage(e));
            return;
        }

        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Registration success");
        dialog.setHeaderText("Account created successfully!");
        dialog.setContentText("You can log in now!");
        dialog.show();
        dialog.setOnCloseRequest(event -> {
            Scene loginScene = new Scene(fxWeaver.loadView(LoginController.class));
            stage.setScene(loginScene);
        });

        clearForm();
    }

    @FXML
    private void onButtonLogin() {
        Scene loginScene = new Scene(fxWeaver.loadView(LoginController.class));
        stage.setScene(loginScene);
    }

    private String getCauseMessage(Throwable t){
        Throwable cause = t;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause.getLocalizedMessage();
    }

    private void clearForm() {
        textFieldFirstName.setText("");
        textFieldLastName.setText("");
        textFieldEmail.setText("");
        textFieldPassword.setText("");
    }

    public void showErrorDialog(String info){
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Error");
        dialog.setHeaderText("Error occurred while creating an account!");
        dialog.setContentText(info);
        dialog.show();
    }

}
