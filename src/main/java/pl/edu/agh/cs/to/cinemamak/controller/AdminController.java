package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.event.ControlPanelSelectionChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.RoleName;
import pl.edu.agh.cs.to.cinemamak.model.User;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

@Component
@FxmlView("admin-view.fxml")
public class AdminController implements ApplicationListener<ControlPanelSelectionChangeEvent> {
    @FXML
    private ListView<User> usersListView;
    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private Button roleSetButton;
    @FXML
    private Button exitButton;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private Stage stage;
    private final UserService userService;
    private final SessionService sessionService;
    private final FxWeaver fxWeaver;

    public AdminController(UserService userService, SessionService sessionService, FxWeaver fxWeaver) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
    }

    public void  initialize() {
        this.roleChoiceBox.getItems().add((RoleName.Admin.toString()));
        this.roleChoiceBox.getItems().add((RoleName.Manager.toString()));
        this.roleChoiceBox.getItems().add((RoleName.Employee.toString()));

        userService.getUsers().ifPresent( listU -> listU.forEach(user -> this.usersListView.getItems().add(user)));

        this.usersListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    this.setText(null);
                } else {
                    this.setText(item.getFirstName() + " " + item.getLastName() + "\n" + (item.getEmailAddress()) + "\n" + item.getRole().getRoleName().toString());
                }
            }
        });

        this.usersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> applicationEventPublisher.publishEvent(new ControlPanelSelectionChangeEvent(this)));
    }

    public void setStage(Stage s) {
        this.stage = s;
    }

    @FXML
    private void onClickExit() {
        stage.setScene(new Scene(fxWeaver.loadView(HomeController.class), 616, 433));
    }

    @FXML
    private void onClickSet() {
        if (this.roleChoiceBox.getValue() != null) {
            if (this.usersListView.getSelectionModel().getSelectedItem() != null) {
                User u = this.usersListView.getSelectionModel().getSelectedItem();
                this.userService.getRoleFromName(RoleName.getEnum(this.roleChoiceBox.getValue()).toString()).ifPresent(role -> {
                    u.setRole(role);
                    this.userService.updateUser(u);
                    this.usersListView.refresh();
                });
            }
        }
    }

    @Override
    public void onApplicationEvent(ControlPanelSelectionChangeEvent event) {
        if(this.usersListView.getSelectionModel().getSelectedItem() != null)
            this.roleChoiceBox.setValue(this.usersListView.getSelectionModel().getSelectedItem().getRole().getRoleName().toString());
    }
}
