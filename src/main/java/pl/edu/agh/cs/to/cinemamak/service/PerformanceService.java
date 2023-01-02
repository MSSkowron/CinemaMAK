package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.repository.PerformanceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PerformanceService {

    PerformanceRepository performanceRepository;

    public PerformanceService(PerformanceRepository performanceRepository){
        this.performanceRepository = performanceRepository;
    }

    public void addPerformance(Performance performance){
        this.performanceRepository.save(performance);
    }

    public Optional<List<Performance>> getPerformances(){
        return Optional.of(this.performanceRepository.findAll());
    }

    public Optional<List<Performance>> getPerformancesByMovieId(long id){
        return Optional.of(this.performanceRepository.findAll().stream().filter(performance -> performance.getMovie().getId() == id).toList());
    }

    public void deletePerformanceById(long id){
        this.performanceRepository.deleteById(id);
    }
}
