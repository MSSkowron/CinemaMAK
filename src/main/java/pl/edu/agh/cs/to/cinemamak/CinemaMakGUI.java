package pl.edu.agh.cs.to.cinemamak;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pl.edu.agh.cs.to.cinemamak.controller.*;

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

        setPrimaryStage(stage, fxWeaver);

        Scene loginScene = new Scene(fxWeaver.loadView(LoginController.class));

        stage.setTitle("CinemaMAK");
        stage.setScene(loginScene);
        stage.show();
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

    private void setPrimaryStage(Stage stage, FxWeaver fxWeaver) {
        fxWeaver.loadController(LoginController.class).setStage(stage);
        fxWeaver.loadController(RegisterController.class).setStage(stage);
        fxWeaver.loadController(HomeController.class).setStage(stage);
        fxWeaver.loadController(AdminController.class).setStage(stage);
        fxWeaver.loadController(MovieController.class).setStage(stage);
    }
}
