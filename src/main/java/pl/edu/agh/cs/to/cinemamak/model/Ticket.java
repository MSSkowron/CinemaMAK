package pl.edu.agh.cs.to.cinemamak.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @OneToOne
    @JoinColumn(name = "performance_id", referencedColumnName = "id")
    private Performance performance;

    @OneToOne
    @JoinColumn(name = "seat_id", referencedColumnName = "id")
    private Seat seat;

    private LocalDateTime soldDatetime;

    public Ticket(Performance performance, Seat seat, LocalDateTime soldDatetime) {
        this.performance = performance;
        this.seat = seat;
        this.soldDatetime = soldDatetime;
    }

    public Ticket() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", performance=" + performance +
                ", seat=" + seat +
                '}';
    }
}
