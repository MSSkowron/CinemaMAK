package pl.edu.agh.cs.to.cinemamak.configs;

import javafx.stage.Stage;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CinemaMakApplicationConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Lazy()
    public Stage primaryStage(Stage stage) {
        return stage;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}