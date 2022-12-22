package pl.edu.agh.cs.to.cinemamak;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CinemaMakApp {
    public static void main(String[] args) {
        Application.launch(CinemaMakGUI.class, args);
    }
}
