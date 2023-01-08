package pl.edu.agh.cs.to.cinemamak.model;

public interface ITableEntityWithMovie {

    long getId();
    void setId(long id);
    Movie getMovie();
    void setMovie(Movie movie);
}
