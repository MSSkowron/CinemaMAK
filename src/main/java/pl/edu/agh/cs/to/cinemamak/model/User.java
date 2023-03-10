package pl.edu.agh.cs.to.cinemamak.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="email_address", nullable = false, unique = true)
    private String emailAddress;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name="role_id", nullable = false)
    private Role role;

    @Column(name="password",nullable = false)
    private String password;


    public User(String firstName, String lastName, String emailAddress, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.emailAddress = emailAddress;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long userID) {
        this.id = userID;
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

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(!(obj instanceof User u)) {
            return false;
        }

        return u.getFirstName().equals(this.getFirstName()) &&
                u.getLastName().equals(this.getLastName()) &&
                u.getPassword().equals(this.getPassword()) &&
                u.getEmailAddress().equals(this.getEmailAddress())&&
                u.getRole().equals(this.getRole());
    }

    @Override
    public int hashCode() {
        int hashCode = this.id.hashCode();
        hashCode = 31 * hashCode + this.emailAddress.hashCode();
        hashCode = 31 * hashCode + this.firstName.hashCode();
        hashCode = 31 * hashCode + this.lastName.hashCode();
        hashCode = 31 * hashCode + this.password.hashCode();
        return hashCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", emailAddress='" + emailAddress + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
