package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Genre;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.repository.GenreRepository;
import pl.edu.agh.cs.to.cinemamak.repository.MovieRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
    }

    public Optional<List<Movie>> getMovies(){
        return Optional.of(movieRepository.findAll());
    }
    public Optional<List<Genre>> getGenres(){
        return Optional.of(genreRepository.findAll());
    }
}
