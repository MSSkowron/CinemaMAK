package pl.edu.agh.cs.to.cinemamak.dto;

public class RoomDto {
    private long room_id;
    private char name;
    private int no_seats;

    public long getRoom_id() {
        return room_id;
    }

    public void setRoom_id(long room_id) {
        this.room_id = room_id;
    }

    public char getName() {
        return name;
    }

    public void setName(char name) {
        this.name = name;
    }

    public int getNo_seats() {
        return no_seats;
    }

    public void setNo_seats(int no_seats) {
        this.no_seats = no_seats;
    }
}
