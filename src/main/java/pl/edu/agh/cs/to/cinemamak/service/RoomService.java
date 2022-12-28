package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Room;
import pl.edu.agh.cs.to.cinemamak.repository.RoomRepository;
import pl.edu.agh.cs.to.cinemamak.repository.SeatRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    SeatRepository seatRepository;
    RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository, SeatRepository seatRepository){
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
    }

    public void addRoom(Room room){
        this.roomRepository.save(room);
    }

    public Optional<List<Room>> getRooms(){
        return Optional.of(this.roomRepository.findAll());
    }

    public Optional<Room> getRoomById(long id){
        return this.roomRepository.getRoomById(id);
    }

}
