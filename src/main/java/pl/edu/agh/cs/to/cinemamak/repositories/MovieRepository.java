package pl.edu.agh.cs.to.cinemamak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.cs.to.cinemamak.models.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
