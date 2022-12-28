package pl.edu.agh.cs.to.cinemamak.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.cs.to.cinemamak.model.Room;
import pl.edu.agh.cs.to.cinemamak.model.Seat;

import java.util.Collection;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Collection<Seat> getSeatByRoom(Room room);
}
