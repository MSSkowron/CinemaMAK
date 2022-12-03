package pl.edu.agh.cs.to.cinemamak.model;

import javax.persistence.*;

@Entity
@Table(name="rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long room_id;

    @Column(name="name", nullable = false)
    private char name;

    @Column(name = "no_seats", nullable = false)
    private int no_seats;
}
