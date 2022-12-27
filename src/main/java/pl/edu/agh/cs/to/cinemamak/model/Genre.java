package pl.edu.agh.cs.to.cinemamak.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name="name",nullable = false, unique = true)
    private String genreName;

    @OneToMany(mappedBy = "genre")
    private Set<Movie> movies;

    public Genre(String roleName) {
        this.genreName = roleName;
    }

    public Genre() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", genreName='" + genreName + '\'' +
                ", movies=" + movies +
                '}';
    }
}