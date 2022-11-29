package pl.edu.agh.cs.to.cinemamak;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ViewInit implements ApplicationListener<UIApplication.StageReadyEvent> {
    @Override
    public void onApplicationEvent(UIApplication.StageReadyEvent event) {
        Stage primaryStage = event.getStage();

        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction((ActionEvent event1) -> {
            System.out.println("Hello World!");
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        primaryStage.setTitle("Cinema Application");
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
