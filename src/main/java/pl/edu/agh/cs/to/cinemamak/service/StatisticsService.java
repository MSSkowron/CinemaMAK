package pl.edu.agh.cs.to.cinemamak.service;

import javafx.util.Pair;
import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Genre;
import pl.edu.agh.cs.to.cinemamak.repository.MovieRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    private final MovieRepository movieRepository;

    public StatisticsService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Collection<Pair<Genre, Double>> getMovieCountsByGenre() {
        var total = movieRepository.count();
        return movieRepository.getMovieCountsByGenre()
                .stream()
                .map(pair -> new Pair<>(pair.getKey(), pair.getValue().doubleValue() / total))
                .collect(Collectors.toSet());
    }
}
