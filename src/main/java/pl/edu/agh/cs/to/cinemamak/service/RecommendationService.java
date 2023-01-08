package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Genre;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.repository.RecommendationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService implements ITableEntityService<Recommendation> {
    RecommendationRepository recommendationRepository;

    public RecommendationService(RecommendationRepository recommendationRepository){
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public void addEntity(Recommendation entity) {
        this.recommendationRepository.save(entity);
    }

    @Override
    public Optional<List<Recommendation>> getEntities() {
        return Optional.of(this.recommendationRepository.findAll());
    }

    @Override
    public Optional<List<Recommendation>> getEntitiesByMovieId(long id) {
        return Optional.of(this.recommendationRepository.findAll().stream().filter(recommendation -> recommendation.getMovie().getId() == id).toList());
    }

    @Override
    public void deleteEntityById(long id) {
        this.recommendationRepository.deleteById(id);
    }

}
