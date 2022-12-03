package pl.edu.agh.cs.to.cinemamak.dto;

import pl.edu.agh.cs.to.cinemamak.model.Seat;

import java.util.Set;

public class RoomDto {
    private long room_id;
    private String name;

    private Set<Seat> seats;

    public long getRoom_id() {
        return room_id;
    }

    public void setRoom_id(long room_id) {
        this.room_id = room_id;
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

}
