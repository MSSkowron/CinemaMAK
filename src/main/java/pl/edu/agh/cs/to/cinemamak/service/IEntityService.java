package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Recommendation;
import pl.edu.agh.cs.to.cinemamak.repository.RecommendationRepository;

import java.util.List;
import java.util.Optional;

@Service
public interface IEntityService<EntityType> {

    void addEntity(EntityType entity);

    Optional<List<EntityType>> getEntities();

    Optional<List<EntityType>> getEntitiesByMovieId(long id);

    void deleteEntityById(long id);


}
