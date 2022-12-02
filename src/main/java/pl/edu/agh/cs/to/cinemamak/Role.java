package pl.edu.agh.cs.to.cinemamak;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_id")
    private long roleID;

    @Column(name="name",nullable = false, unique = true)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private Set<User> users;

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Role() {

    }
}
