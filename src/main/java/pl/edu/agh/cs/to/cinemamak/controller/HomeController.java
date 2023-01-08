package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.application.Platform;
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
    private BorderPane borderPane;
    private double x = 0;
    private double y = 0;
    @FXML
    private AnchorPane ap;
    @FXML
    public Label homeViewUserLabel;
    @FXML
    private Label helloLabel;
    @FXML
    public Button movieViewButton;
    @FXML
    private Button adminViewButton;

    @FXML
    private Button performanceButton;
    @FXML
    private Button recommendationsButton;
    @FXML
    private Button ticketsViewButton;
    @FXML
    private Button statisticsButton;

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
        homeViewUserLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            if (sessionService.getCurrentUser().isEmpty()) {
                return "";
            }

            User currUser = sessionService.getCurrentUser().get();
            return "Hello %s!".formatted(currUser.getFirstName());
        }, sessionService.getCurrentUserProperty()));

        helloLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            if (sessionService.getCurrentUser().isEmpty()) {
                return "";
            }

            User currUser = sessionService.getCurrentUser().get();
            return "%s %s".formatted(currUser.getFirstName(), currUser.getLastName());
        }, sessionService.getCurrentUserProperty()));

        roleUIHelper.bindVisibleOnlyToRoles(adminViewButton, RoleName.Admin);
        roleUIHelper.bindVisibleOnlyToRoles(performanceButton, RoleName.Admin, RoleName.Manager);
        roleUIHelper.bindVisibleOnlyToRoles(movieViewButton, RoleName.Admin, RoleName.Manager);
        roleUIHelper.bindVisibleOnlyToRoles(recommendationsButton, RoleName.Admin, RoleName.Manager);
        roleUIHelper.bindVisibleOnlyToRoles(statisticsButton, RoleName.Manager, RoleName.Admin);
    }

    @FXML
    public void home(javafx.scene.input.MouseEvent mouseEvent) {
        borderPane.setCenter(ap);
    }

    @FXML
    private void adminView(javafx.scene.input.MouseEvent mouseEvent) {
        Parent root;
        root = fxWeaver.loadView(AdminController.class);
        borderPane.setCenter(root);
    }

    @FXML
    private void performanceView(javafx.scene.input.MouseEvent mouseEvent){
        Parent root;
        root = fxWeaver.loadView(PerformanceController.class);
        borderPane.setCenter(root);
    }

    @FXML
    private void movieView(javafx.scene.input.MouseEvent mouseEvent) {
        Parent root;
        root = fxWeaver.loadView(MovieController.class);
        borderPane.setCenter(root);
    }

    @FXML
    private void ticketsView(MouseEvent ignored) {
        var root = fxWeaver.loadView(TicketsController.class);
        borderPane.setCenter(root);
    }

    @FXML
    private void statisticsView(MouseEvent ignored) {
        var root = fxWeaver.loadView(StatisticsController.class);
        borderPane.setCenter(root);
    }

    @FXML
    public void logOut(javafx.scene.input.MouseEvent mouseEvent) {
        sessionService.endSession();

        Scene loginScene = new Scene(fxWeaver.loadView(LoginController.class));
        stage.setScene(loginScene);
    }

    public void recommendationsView(MouseEvent mouseEvent) {
        Parent root;
        root = fxWeaver.loadView(RecommendationsController.class);
        borderPane.setCenter(root);
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

    public void onButtonMinimize(MouseEvent event) {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setIconified(true);
    }

    public void onButtonMaximize(MouseEvent event) {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }
}
