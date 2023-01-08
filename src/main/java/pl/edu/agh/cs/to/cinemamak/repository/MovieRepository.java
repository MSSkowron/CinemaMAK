package pl.edu.agh.cs.to.cinemamak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.cs.to.cinemamak.model.Genre;
import pl.edu.agh.cs.to.cinemamak.model.Movie;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> getMovieById(long id);
    Optional<List<Movie>> getMoviesByTitleContainingOrDirectorContainingOrDateBetweenOrGenre(String title, String director, LocalDateTime date, LocalDateTime date2, Genre genre);
}
