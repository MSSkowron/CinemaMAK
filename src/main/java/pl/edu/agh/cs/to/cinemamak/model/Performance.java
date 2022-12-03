package pl.edu.agh.cs.to.cinemamak.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name="performances")
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @OneToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @OneToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    @Column(name="date", nullable = false)
    private Date date;

    @Column(name="price", nullable = false)
    private BigDecimal price;

    @OneToOne
    @JoinColumn(name = "supervisor_id", referencedColumnName = "id")
    private User user;

    public Performance(Movie movie, Room room, Date date, BigDecimal price, User user) {
        this.movie = movie;
        this.room = room;
        this.date = date;
        this.price = price;
        this.user = user;
    }

    public Performance() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
