package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.RoleName;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final SessionService sessionService;
    private final  UserService userService;

    public EmailService(JavaMailSender mailSender, SessionService sessionServicem, UserService userService) {
        this.mailSender = mailSender;
        this.sessionService = sessionServicem;
        this.userService = userService;
    }

    public void sendToUser(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText("Sent from user: " + sessionService.getCurrentUser().get().getEmailAddress() + "\n" + body);

        mailSender.send(message);
    }

    public void sendToEmployees(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText("Sent from user: " + sessionService.getCurrentUser().get().getEmailAddress() + "\n" + body);

        userService.getUsers().ifPresent(listU -> listU.forEach(user -> {
            if (user.getRole().getRoleName().equals(RoleName.Employee)) {
                message.setTo(user.getEmailAddress());
                mailSender.send(message);
            }
        }));
    }

    public void sendToAdmins(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText("Sent from user: " + sessionService.getCurrentUser().get().getEmailAddress() + "\n" + body);

        userService.getUsers().ifPresent(listU -> listU.forEach(user -> {
            if (user.getRole().getRoleName().equals(RoleName.Admin)) {
                message.setTo(user.getEmailAddress());
                mailSender.send(message);
            }
        }));
    }

    public void sendToManagers(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText("Sent from user: " + sessionService.getCurrentUser().get().getEmailAddress() + "\n" + body);

        userService.getUsers().ifPresent(listU -> listU.forEach(user -> {
            if (user.getRole().getRoleName().equals(RoleName.Manager)) {
                message.setTo(user.getEmailAddress());
                mailSender.send(message);
            }
        }));
    }
}
