package pl.edu.agh.cs.to.cinemamak;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.cs.to.cinemamak.model.RoleName;

@SpringBootApplication
public class CinemaMakApp {
    public static void main(String[] args) {
        Application.launch(CinemaMakGUI.class, args);
    }
}
