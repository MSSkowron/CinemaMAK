package pl.edu.agh.cs.to.cinemamak.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.cs.to.cinemamak.model.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
