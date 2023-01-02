package pl.edu.agh.cs.to.cinemamak.mails;

import pl.edu.agh.cs.to.cinemamak.service.SessionService;
import pl.edu.agh.cs.to.cinemamak.service.UserService;

public class MailSender {
    private final SessionService sessionService;
    private final UserService userService;
    public MailSender(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }
}
