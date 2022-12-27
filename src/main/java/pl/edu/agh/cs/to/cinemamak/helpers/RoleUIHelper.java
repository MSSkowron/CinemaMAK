package pl.edu.agh.cs.to.cinemamak.helpers;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import pl.edu.agh.cs.to.cinemamak.model.RoleName;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;

import java.util.Arrays;

public class RoleUIHelper {
    private final SessionService sessionService;
    public RoleUIHelper(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void bindVisibleOnlyToRoles(Node element, RoleName... roles) {
        var binding = Bindings.createBooleanBinding(
                () -> sessionService.getCurrentUser().isPresent() &&
                    Arrays.stream(roles)
                            .anyMatch(roleName -> roleName == sessionService.getCurrentUser().get().getRole().getRoleName()),
                sessionService.getCurrentUserProperty());

        element.visibleProperty().bind(binding);
        element.managedProperty().bind(binding);
    }
}
