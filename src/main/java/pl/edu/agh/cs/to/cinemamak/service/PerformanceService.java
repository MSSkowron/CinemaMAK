package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.repository.PerformanceRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PerformanceService implements ITableEntityService<Performance>{

    PerformanceRepository performanceRepository;

    public PerformanceService(PerformanceRepository performanceRepository){
        this.performanceRepository = performanceRepository;
    }

    @Override
    public void addEntity(Performance entity) {
        this.performanceRepository.save(entity);
    }

    @Override
    public Optional<List<Performance>> getEntities() {
        return Optional.of(this.performanceRepository.findAll());
    }

    @Override
    public Optional<List<Performance>> getEntitiesByMovieId(long id) {
        return Optional.of(this.performanceRepository.findAll().stream().filter(performance -> performance.getMovie().getId() == id).toList());
    }

    public Collection<Performance> getPerformancesAfterToday() {
        return performanceRepository.getPerformancesByDateAfter(LocalDateTime.now());
    }

        @Override
        public void deleteEntityById(long id) {
        this.performanceRepository.deleteById(id);
    }
}
