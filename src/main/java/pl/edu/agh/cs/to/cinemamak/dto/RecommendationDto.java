package pl.edu.agh.cs.to.cinemamak.dto;

import pl.edu.agh.cs.to.cinemamak.model.Movie;
import java.sql.Date;

public class RecommendationDto {
    private long id;
    private Movie movie;
    private Date dateFrom;
    private Date dateTo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
