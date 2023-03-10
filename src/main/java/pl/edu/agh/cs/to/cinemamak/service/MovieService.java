package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.*;
import pl.edu.agh.cs.to.cinemamak.repository.GenreRepository;
import pl.edu.agh.cs.to.cinemamak.repository.MovieRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService implements ITableEntityService<Movie>{
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
    public Optional<Genre> getGenreByName(String name) {
        return genreRepository.findGenreByGenreName(name);
    }

    public void addMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }
    public Optional<Movie> getMovieById(long id){
        return movieRepository.getMovieById(id);
    }

    @Override
    public void addEntity(Movie entity) {
        movieRepository.save(entity);
    }

    @Override
    public Optional<List<Movie>> getEntities() {
        return Optional.of(movieRepository.findAll());
    }

    @Override
    public Optional<List<Movie>> getEntitiesByMovieId(long id) {
        return Optional.of(movieRepository.getMovieById(id).stream().toList());
    }

    @Override
    public void deleteEntityById(long id) {
        movieRepository.deleteById(id);
    }

    public Optional<List<Movie>> getListOfEntitiesWithParticularMovie(String title, String director, int year, Genre genre){
        LocalDateTime localDateTime1 = LocalDateTime.of(LocalDate.of(year,1,1), LocalTime.of(1,0));
        LocalDateTime localDateTime2 = LocalDateTime.of(LocalDate.of(year,12,31), LocalTime.of(23,59));
        return this.movieRepository.getMoviesByTitleContainingOrDirectorContainingOrDateBetweenOrGenre(title, director, localDateTime1, localDateTime2, genre);
    }

}
