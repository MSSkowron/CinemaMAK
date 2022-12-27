package pl.edu.agh.cs.to.cinemamak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.cs.to.cinemamak.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
