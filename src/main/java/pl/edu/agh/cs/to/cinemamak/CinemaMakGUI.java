package pl.edu.agh.cs.to.cinemamak;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pl.edu.agh.cs.to.cinemamak.controller.HomeController;
import pl.edu.agh.cs.to.cinemamak.controller.LoginController;
import pl.edu.agh.cs.to.cinemamak.controller.RegisterController;

public class CinemaMakGUI extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(CinemaMakApp.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(LoginController.class);
        Scene scene = new Scene(root,616, 433);
        stage.setTitle("CinemaMAK");
        stage.setScene(scene);

        setPrimaryStage(stage, fxWeaver);

        stage.show();
    }

    private void setPrimaryStage(Stage stage, FxWeaver fxWeaver) {

        fxWeaver.loadController(LoginController.class).setStage(stage);
        fxWeaver.loadController(RegisterController.class).setStage(stage);
        fxWeaver.loadController(HomeController.class).setStage(stage);
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }
}
