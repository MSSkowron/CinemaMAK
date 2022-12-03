package pl.edu.agh.cs.to.cinemamak.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name="date", nullable = false)
    private Date date;

    @Column(name="duration", nullable = false)
    private int duration;

    @Column(name="director", nullable = false)
    private String director;

    @ManyToOne
    @JoinColumn(name="genre_id", nullable = false)
    private Genre genre;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="average_rating", nullable = true)
    private float averageRating;

    @Column(name="rate_count", nullable = true)
    private int rateCount;
}
