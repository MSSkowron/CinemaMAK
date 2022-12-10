package pl.edu.agh.cs.to.cinemamak.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public RoleName getRoleName() {
        switch(roleName){
            case "Admin" -> {
                return RoleName.Admin;
            }
            case "Manager" -> {
                return RoleName.Manager;
            }
            default -> {
                return RoleName.Employee;
            }
        }
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName.toString();
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Column(name="name",nullable = false, unique = true)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private Set<User> users;

    public Role(RoleName roleName) {
        this.roleName = roleName.toString();
    }
    public Role() {

    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Role)) return false;

        Role r = (Role)obj;

        return r.getRoleName().equals(this.getRoleName());
    }
}
