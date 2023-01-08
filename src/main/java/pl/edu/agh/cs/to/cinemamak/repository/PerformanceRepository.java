package pl.edu.agh.cs.to.cinemamak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.cs.to.cinemamak.model.Performance;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    Collection<Performance> getPerformancesByDateAfter(LocalDateTime date);
}
