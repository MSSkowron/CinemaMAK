package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.helpers.RoleUIHelper;
import pl.edu.agh.cs.to.cinemamak.model.RoleName;
import pl.edu.agh.cs.to.cinemamak.model.User;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;

@Component
@FxmlView("home-view.fxml")
public class HomeController {
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;
    @FXML
    private Label helloLabel;
    @FXML
    public Button movieViewButton;
    @FXML
    private Button adminViewButton;

    @FXML
    private Button performanceButton;
    @FXML
    private Button ticketsViewButton;

    private final SessionService sessionService;
    private final FxWeaver fxWeaver;
    private Stage stage;
    private final RoleUIHelper roleUIHelper;

    public HomeController(SessionService sessionService, FxWeaver fxWeaver, RoleUIHelper roleUIHelper) {
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
        this.roleUIHelper = roleUIHelper;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        helloLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            if (sessionService.getCurrentUser().isEmpty()) {
                return "";
            }

            User currUser = sessionService.getCurrentUser().get();
            return "%s %s".formatted(currUser.getFirstName(), currUser.getLastName());
        }, sessionService.getCurrentUserProperty()));

        roleUIHelper.bindVisibleOnlyToRoles(adminViewButton, RoleName.Admin);
    }

    @FXML
    public void home(javafx.scene.input.MouseEvent mouseEvent) {
        bp.setCenter(ap);
    }

    @FXML
    private void adminView(javafx.scene.input.MouseEvent mouseEvent) {
        Parent root;
        root = fxWeaver.loadView(AdminController.class);
        bp.setCenter(root);
    }

    @FXML
    private void performanceView(javafx.scene.input.MouseEvent mouseEvent){
        Parent root;
        root = fxWeaver.loadView(PerformanceController.class);
        bp.setCenter(root);
    }

    @FXML
    private void movieView(javafx.scene.input.MouseEvent mouseEvent) {
        Parent root;
        root = fxWeaver.loadView(MovieController.class);
        bp.setCenter(root);
    }

    @FXML
    private void ticketsView(MouseEvent ignored) {
        var root = fxWeaver.loadView(TicketsController.class);
        bp.setCenter(root);
    }

    @FXML
    public void logOut(javafx.scene.input.MouseEvent mouseEvent) {
        sessionService.endSession();

        Scene loginScene = new Scene(fxWeaver.loadView(LoginController.class));
        stage.setScene(loginScene);
    }
}
