package pl.edu.agh.cs.to.cinemamak.controller;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.Role;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

@Component
@FxmlView("admin-view.fxml")
public class AdminController {

    @FXML
    private ListView<Button> usersListView;
    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private Button roleSetButton;

    @FXML
    private Button exitButton;


    private Stage stage;

    private UserService userService;
    private SessionService sessionService;
    private FxWeaver fxWeaver;

    public AdminController(UserService userService,
                           SessionService sessionService,
                           FxWeaver fxWeaver){

        this.userService = userService;
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;

//        this.initialize();
    }

//    public void initialize(){
//
//        SimpleListProperty<String> list = new SimpleListProperty<String>();
//        list.add("manager");
//        list.add("employee");
//        this.roleChoiceBox.setItems(list);
//
//        SimpleListProperty<Button> listBtn = new SimpleListProperty<>();
//
//        userService.getUsers().ifPresent( listU ->{
//            listU.stream().forEach(user ->{
//                listBtn.add(new Button(user.getEmailAddress()));
//            });
//        });
//
//        this.usersListView.setItems(listBtn);
//    }

    public void setStage(Stage s){
        this.stage = s;
    }

    @FXML
    private void onClickExit() {
        stage.setScene(new Scene(fxWeaver.loadView(HomeController.class), 616, 433));
    }

    @FXML
    private void onClickSet(){
        String role = this.roleChoiceBox.getValue();
        System.out.println(role);
    }

}
