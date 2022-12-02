package pl.edu.agh.cs.to.cinemamak;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class JavaFxApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    private FXMLLoader fxmlLoader;
    private Scene scene;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(CinemaMakApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) {

        try {
            fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("login-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 616, 433);
            stage.setTitle("Cinema Application");
            stage.setScene(scene);
            stage.show();

        } catch( IOException exception){
            exception.getStackTrace();
        }

    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }
}
