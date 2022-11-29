package pl.edu.agh.cs.to.cinemamak;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class CinemaMakApplication {

    public static void main(String[] args) {
//        SpringApplication.run(CinemaMakApplication.class, args);
        Application.launch(UIApplication.class, args);
    }

}
