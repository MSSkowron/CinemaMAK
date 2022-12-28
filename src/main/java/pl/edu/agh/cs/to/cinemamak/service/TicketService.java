package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.model.Seat;
import pl.edu.agh.cs.to.cinemamak.model.Ticket;
import pl.edu.agh.cs.to.cinemamak.repository.RoomRepository;
import pl.edu.agh.cs.to.cinemamak.repository.SeatRepository;
import pl.edu.agh.cs.to.cinemamak.repository.TicketRepository;

import java.util.Optional;

@Service
public class TicketService {

    TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

    public void addSoldTicket(Ticket ticket){
        this.ticketRepository.save(ticket);
    }

    public void removeTicket(Ticket ticket){
        this.ticketRepository.delete(ticket);
    }

    public boolean isSeatTaken(Performance performance, Seat seat) {
        return ticketRepository.findTicketByPerformanceAndSeat(performance, seat).isPresent();
    }

    public Optional<Ticket> getTicket(Performance p, Seat s) {
        return ticketRepository.findTicketByPerformanceAndSeat(p, s);
    }
}
