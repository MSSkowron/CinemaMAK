package pl.edu.agh.cs.to.cinemamak.dto;

import pl.edu.agh.cs.to.cinemamak.models.Movie;

import java.util.Set;

public class GenreDto {
    private long id;
    private String genreName;
    private Set<Movie> movies;

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
}
