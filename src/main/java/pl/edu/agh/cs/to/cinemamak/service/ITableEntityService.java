package pl.edu.agh.cs.to.cinemamak.service;

import java.util.List;
import java.util.Optional;


public interface ITableEntityService<EntityType> {

    void addEntity(EntityType entity);

    Optional<List<EntityType>> getEntities();

    Optional<List<EntityType>> getEntitiesByMovieId(long id);

    void deleteEntityById(long id);

}
