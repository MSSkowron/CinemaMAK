package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.repository.PerformanceRepository;
import pl.edu.agh.cs.to.cinemamak.repository.RecommendationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    RecommendationRepository recommendationRepository;

    public RecommendationService(RecommendationRepository recommendationRepository){
        this.recommendationRepository = recommendationRepository;
    }

    public void addRecommendation(Recommendation recommendation){
        this.recommendationRepository.save(recommendation);
    }

    public Optional<List<Recommendation>> getRecommendations(){
        return Optional.of(this.recommendationRepository.findAll());
    }

    public Optional<List<Recommendation>> getRecommendationsByMovieId(long id){
        return Optional.of(this.recommendationRepository.findAll().stream().filter(recommendation -> recommendation.getMovie().getId() == id).toList());
    }

    public void deleteRecommendationById(long id){
        this.recommendationRepository.deleteById(id);
    }

}
