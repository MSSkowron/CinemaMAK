package pl.edu.agh.cs.to.cinemamak.model;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name="recommendations")
public class Recommendation implements ITableEntityWithMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    protected long id;

    @OneToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    protected Movie movie;

    @Column(name="date_from", nullable = false)
    private LocalDateTime dateFrom;

    @Column(name="date_to", nullable = false)
    private LocalDateTime dateTo;

    public Recommendation(){
    }

    public Recommendation(Movie movie, LocalDateTime dateFrom, LocalDateTime dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.movie = movie;
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

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "id=" + id +
                ", movie=" + movie +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }
}
