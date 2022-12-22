package pl.edu.agh.cs.to.cinemamak.model;

import javax.persistence.*;

@Entity
@Table(name="seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name="name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="room_id", nullable = false)
    private Room room;

    public Seat(String name, Room room) {
        this.name = name;
        this.room = room;
    }


    public Seat() {

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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
