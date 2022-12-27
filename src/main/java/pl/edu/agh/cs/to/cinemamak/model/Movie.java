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

    @Column(name="image_url", nullable = false)
    private String imageURL;

    @Column(name="duration", nullable = false)
    private int duration;

    @ManyToOne
    @JoinColumn(name="genre_id", nullable = false)
    private Genre genre;

    @Column(name="date", nullable = false)
    private Date date;

    public Movie(String title, String director, String description, int duration, Genre genre, Date date, String imageURL) {
        this.title = title;
        this.director = director;
        this.description = description;
        this.duration = duration;
        this.genre = genre;
        this.date = date;
        this.imageURL = imageURL;
    }

    public Movie() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
