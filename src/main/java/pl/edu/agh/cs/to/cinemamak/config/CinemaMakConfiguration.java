package pl.edu.agh.cs.to.cinemamak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.agh.cs.to.cinemamak.helpers.RoleUIHelper;
import pl.edu.agh.cs.to.cinemamak.mails.MailSender;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

@Configuration
public class CinemaMakConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleUIHelper roleUIHelper(SessionService sessionService) {
        return new RoleUIHelper(sessionService);
    }

    @Bean
    public MailSender mailSender(SessionService sessionService, UserService userService) {
        return new MailSender(sessionService, userService);
    }
}