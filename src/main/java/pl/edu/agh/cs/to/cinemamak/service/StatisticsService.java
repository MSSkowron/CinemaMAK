package pl.edu.agh.cs.to.cinemamak.service;

import javafx.util.Pair;
import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Genre;
import pl.edu.agh.cs.to.cinemamak.repository.MovieRepository;
import pl.edu.agh.cs.to.cinemamak.repository.PerformanceRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    private final MovieRepository movieRepository;
    private final PerformanceRepository performanceRepository;

    public StatisticsService(MovieRepository movieRepository, PerformanceRepository performanceRepository) {
        this.movieRepository = movieRepository;
        this.performanceRepository = performanceRepository;
    }

    public Collection<Pair<Genre, Double>> getMovieCountsByGenre() {
        var total = movieRepository.count();
        return movieRepository.getMovieCountsByGenre()
                .stream()
                .map(pair -> new Pair<>(pair.getKey(), pair.getValue().doubleValue() / total))
                .collect(Collectors.toSet());
    }

    public Collection<Pair<Genre, Double>> getPlannedPerformanceCountsByGenre() {
        var now = LocalDateTime.now();
        var total = performanceRepository.countPerformancesByDateAfter(now);
        return performanceRepository.getPerformanceCountsByGenreByDateAfter(now)
                .stream()
                .map(pair -> new Pair<>(pair.getKey(), pair.getValue().doubleValue() / total))
                .collect(Collectors.toSet());
    }
}
