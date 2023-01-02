package pl.edu.agh.cs.to.cinemamak.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.User;
import java.util.Optional;

@Service
public class SessionService {
    private final ObjectProperty<Optional<User>> currentUser = new SimpleObjectProperty<>(Optional.empty());

    public void setCurrentUser(User user) {
        currentUser.set(Optional.of(user));
    }

    public Optional<User> getCurrentUser() {
        return currentUser.get();
    }

    public ObjectProperty<Optional<User>> getCurrentUserProperty() {
        return currentUser;
    }

    public void endSession() {
        currentUser.set(Optional.empty());
    }
}
