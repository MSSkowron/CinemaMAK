package pl.edu.agh.cs.to.cinemamak.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name="name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "room")
    private Set<Seat> seats;

    public Room(String name) {
        this.name = name;
    }

    public Room() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name +
                '}';
    }
}
