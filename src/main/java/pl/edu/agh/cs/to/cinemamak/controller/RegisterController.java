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
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.config.DialogManager;
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
    @FXML
    private Button buttonExit;
    @FXML
    private Button buttonRegister;
    @FXML
    private Button buttonLogin;
    private final UserService userService;
    private final DialogManager dialogManager;
    private Stage stage;
    private final FxWeaver fxWeaver;

    public RegisterController(UserService userService,
                              FxWeaver fxWeaver,
                              DialogManager dialogManager) {
        this.userService = userService;
        this.fxWeaver = fxWeaver;
        this.dialogManager = dialogManager;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onButtonExitClick() {
        Platform.exit();
    }

    @FXML
    private void onButtonRegisterClick() {
        if(textFieldFirstName.getText().isEmpty() || textFieldLastName.getText().isEmpty() || textFieldEmail.getText().isEmpty() || textFieldPassword.getText().isEmpty()) {
            this.dialogManager.showError(stage,"Error occurred while creating an account!","All fields need to be filled!");
            return;
        }

        if(!EmailValidator.getInstance().isValid(textFieldEmail.getText())) {
            this.dialogManager.showError(stage,"Error occurred while creating an account!","Email is not valid!");
            return;
        }

        User newUser = new User(textFieldFirstName.getText(), textFieldLastName.getText(), textFieldEmail.getText(), textFieldPassword.getText());

        try {
            userService.addUser(newUser);
        } catch (Exception|Error e) {
            this.dialogManager.showError(stage,"Error occurred while creating an account!",getCauseMessage(e));
            return;
        }

        this.dialogManager.showInformation(stage,
                "Account created successfully!",
                "You can log in now!", event -> {
                    Scene loginScene = new Scene(fxWeaver.loadView(LoginController.class), 616, 433);
                    stage.setScene(loginScene);
                });
        clearForm();
    }

    @FXML
    private void onButtonLogin() {
        Scene loginScene = new Scene(fxWeaver.loadView(LoginController.class), 616, 433);
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

}
