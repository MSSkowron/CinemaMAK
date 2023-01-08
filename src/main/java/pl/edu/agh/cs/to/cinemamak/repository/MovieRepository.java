package pl.edu.agh.cs.to.cinemamak.repository;

import javafx.util.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.cs.to.cinemamak.model.Genre;
import pl.edu.agh.cs.to.cinemamak.model.Movie;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> getMovieById(long id);
    Optional<List<Movie>> getMoviesByTitleContainingOrDirectorContainingOrDateBetweenOrGenre(String title, String director, LocalDateTime date, LocalDateTime date2, Genre genre);

    @Query("select new javafx.util.Pair(g, count(m.id)) " +
            "from Movie m " +
            "join Genre g on m.genre = g " +
            "group by g.id")
    Collection<Pair<Genre, Long>> getMovieCountsByGenre();

}
