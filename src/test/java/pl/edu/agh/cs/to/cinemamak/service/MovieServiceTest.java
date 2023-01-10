package pl.edu.agh.cs.to.cinemamak.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.cs.to.cinemamak.model.Movie;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Test
    public void getParticularMovies(){
        Optional<List<Movie>> allMovies = movieService.getMovies();
        if(allMovies.isPresent() && !allMovies.get().isEmpty()){
            Movie particularMovie = allMovies.get().get(0);

            Optional<List<Movie>> selectedMovie =
                    movieService.
                            getListOfEntitiesWithParticularMovie(particularMovie.getTitle(), particularMovie.getDirector(), particularMovie.getDate().getYear(), particularMovie.getGenre());

            if(selectedMovie.isPresent() && !selectedMovie.get().isEmpty()) {
                assertEquals(particularMovie.getId(), selectedMovie.get().get(0).getId());
            }
            else {
                fail();
            }
        }
    }

}
