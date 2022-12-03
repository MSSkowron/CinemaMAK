package pl.edu.agh.cs.to.cinemamak.models;

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
}