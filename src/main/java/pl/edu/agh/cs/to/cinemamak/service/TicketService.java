package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Ticket;
import pl.edu.agh.cs.to.cinemamak.repository.RoomRepository;
import pl.edu.agh.cs.to.cinemamak.repository.SeatRepository;
import pl.edu.agh.cs.to.cinemamak.repository.TicketRepository;

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

}
