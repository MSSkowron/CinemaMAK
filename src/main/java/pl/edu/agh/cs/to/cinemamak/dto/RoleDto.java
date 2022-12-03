package pl.edu.agh.cs.to.cinemamak.dto;

import pl.edu.agh.cs.to.cinemamak.model.User;
import java.util.Set;

public class RoleDto {
    private long id;
    private String roleName;
    private Set<User> users;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
