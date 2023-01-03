package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.RoleName;
import pl.edu.agh.cs.to.cinemamak.model.User;

import java.util.Optional;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final SessionService sessionService;
    private final  UserService userService;

    public EmailService(JavaMailSender mailSender, SessionService sessionService, UserService userService) {
        this.mailSender = mailSender;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    public void sendToUser(User user, String subject, String body) {
        Optional<User> sender = sessionService.getCurrentUser();
        if (sender.isEmpty()) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmailAddress());
        message.setSubject(subject);
        message.setText("Sent by: " + sender.get().getEmailAddress() + "\n" + body);

        mailSender.send(message);
    }

    public void sendToUsersWithRole(RoleName roleName, String subject, String body) {
        Optional<User> sender = sessionService.getCurrentUser();
        if (sender.isEmpty()) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText("Sent from user: " + sender.get().getEmailAddress() + "\n" + body);

        userService.getUsers().ifPresent(listU -> listU.forEach(user -> {
            if (user.getRole().getRoleName().equals(roleName)) {
                message.setTo(user.getEmailAddress());
                mailSender.send(message);
            }
        }));
    }

    public void sendToAllUsers(String subject, String body) {
        Optional<User> sender = sessionService.getCurrentUser();
        if (sender.isEmpty()) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText("Sent from user: " + sender.get().getEmailAddress() + "\n" + body);

        userService.getUsers().ifPresent(listU -> listU.forEach(user -> {
            message.setTo(user.getEmailAddress());
            mailSender.send(message);
        }));
    }
}
