package pl.edu.agh.cs.to.cinemamak.repository;

import javafx.util.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.cs.to.cinemamak.model.Genre;
import pl.edu.agh.cs.to.cinemamak.model.Performance;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    Collection<Performance> getPerformancesByDateAfter(LocalDateTime date);
    long countPerformancesByDateAfter(LocalDateTime date);
    @Query("select new javafx.util.Pair(g, count(p.id)) " +
            "from Performance p " +
            "join Movie m on p.movie = m " +
            "join Genre g on m.genre = g " +
            "where p.date > :date " +
            "group by g.id ")
    Collection<Pair<Genre, Long>> getPerformanceCountsByGenreByDateAfter(LocalDateTime date);
}
