package pl.edu.agh.cs.to.cinemamak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.model.Seat;
import pl.edu.agh.cs.to.cinemamak.model.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findTicketByPerformanceAndSeat(Performance performance, Seat seat);

    long countTicketsBySoldDatetimeBetween(LocalDateTime from, LocalDateTime to);

    @Query("select sum(p.price) " +
            "from Ticket t " +
            "join Performance p on t.performance = p " +
            "where t.soldDatetime >= :from and t.soldDatetime < :to")
    Double findTotalProfitBySoldDatetimeBetween(LocalDateTime from, LocalDateTime to);
}
