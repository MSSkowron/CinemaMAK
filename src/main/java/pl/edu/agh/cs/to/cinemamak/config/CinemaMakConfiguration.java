package pl.edu.agh.cs.to.cinemamak.config;

import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.agh.cs.to.cinemamak.mapper.UserMapper;

@Configuration
public class CinemaMakConfiguration {
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
    public UserMapper userMapper(){
        return new UserMapper();
    }
}