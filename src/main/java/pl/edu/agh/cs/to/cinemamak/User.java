package pl.edu.agh.cs.to.cinemamak;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private long userID;
    @Column(name="password",nullable = false, unique = true)
    private String password;

    @Column(name="email_address", nullable = false, unique = true)
    private String emailAddress;

    @ManyToOne
    @JoinColumn(name="role_id", nullable = false)
    private Role role;

    public User(String emailAddress, String password) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
