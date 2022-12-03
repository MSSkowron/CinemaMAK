package pl.edu.agh.cs.to.cinemamak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.cs.to.cinemamak.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
