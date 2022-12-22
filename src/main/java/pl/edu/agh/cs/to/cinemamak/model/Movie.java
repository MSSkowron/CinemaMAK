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

    @Column(name="director", nullable = false)
    private String director;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="duration", nullable = false)
    private int duration;

    @ManyToOne
    @JoinColumn(name="genre_id", nullable = false)
    private Genre genre;

    @Column(name="date", nullable = false)
    private Date date;
}
