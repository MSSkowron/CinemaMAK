package pl.edu.agh.cs.to.cinemamak.model;

import javax.persistence.*;

@Entity
@Table(name="seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column
    private long rowNumber;

    @Column
    private long colNumber;

    @ManyToOne
    @JoinColumn(name="room_id", nullable = false)
    private Room room;

    public Seat(Room room, Long rowNumber, Long colNumber) {
        this.room = room;
        this.rowNumber = rowNumber;
        this.colNumber = colNumber;
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
        char rowTag = (char)('A' + rowNumber - 1);
        return "%c%d".formatted(rowTag, colNumber);
    }

    public long getRowNumber() {
        return rowNumber;
    }

    public long getColNumber() {
        return colNumber;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", name='" + getName() + '\'' +
                ", room=" + room +
                '}';
    }
}
