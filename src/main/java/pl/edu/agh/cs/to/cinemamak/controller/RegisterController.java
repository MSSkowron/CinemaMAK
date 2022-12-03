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
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.dto.UserDto;
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
            System.out.println("All fields need to be filled");
            return;
        }

        if(!EmailValidator.getInstance().isValid(textFieldEmail.getText())){
            System.out.println("Email is not valid");
            return;
        }

        UserDto userDto = new UserDto();
        userDto.setFirstName(textFieldFirstName.getText());
        userDto.setLastName(textFieldLastName.getText());
        userDto.setEmailAddress(textFieldEmail.getText());
        userDto.setPassword(textFieldPassword.getText());

        userService.addUser(userDto);
    }

    @FXML
    private void onButtonLogin() {
        Scene scene = new Scene(fxWeaver.loadView(LoginController.class), 616, 433);
        stage.setScene(scene);
    }
}
