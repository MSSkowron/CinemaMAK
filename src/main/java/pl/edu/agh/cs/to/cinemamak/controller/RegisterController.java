package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
    private BorderPane borderPane;
    private double x = 0;
    private double y = 0;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldPassword;
    private final UserService userService;
    private final DialogManager dialogManager;
    private Stage stage;
    private final FxWeaver fxWeaver;

    public RegisterController(UserService userService, FxWeaver fxWeaver, DialogManager dialogManager) {
        this.userService = userService;
        this.fxWeaver = fxWeaver;
        this.dialogManager = dialogManager;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onButtonRegister() {
        if (textFieldFirstName.getText().isEmpty() || textFieldLastName.getText().isEmpty() || textFieldEmail.getText().isEmpty() || textFieldPassword.getText().isEmpty()) {
            this.dialogManager.showError(stage,"Error occurred while creating an account!","All fields need to be filled!");
            return;
        }

        if (!EmailValidator.getInstance().isValid(textFieldEmail.getText())) {
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

        this.dialogManager.showInformation(stage, "Account created successfully!", "You can log in now!", event -> {
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

    private String getCauseMessage(Throwable t) {
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
