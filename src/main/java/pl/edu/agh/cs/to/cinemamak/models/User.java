package pl.edu.agh.cs.to.cinemamak.models;

import pl.edu.agh.cs.to.cinemamak.models.Role;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private long userID;

    @Column(name="email_address", nullable = false, unique = true)
    private String emailAddress;

    @Column(name="first_name", nullable = false,unique = false)
    private String firstName;

    @Column(name="last_name", nullable = false, unique = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name="role_id", nullable = false)
    private Role role;

    @Column(name="password",nullable = false, unique = true)
    private String password;

    public User(String firstName, String lastName, String emailAddress, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.emailAddress = emailAddress;
    }

    public User() {

    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }


    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}