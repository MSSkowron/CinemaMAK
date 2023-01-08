package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.repository.MovieRepository;
import pl.edu.agh.cs.to.cinemamak.service.StatisticsService;

import java.util.stream.Collectors;


@Component
@FxmlView("statistics-view.fxml")
public class StatisticsController {
    @FXML
    private PieChart movieCountByGenrePieChart;

    private final StatisticsService statisticsService;

    public StatisticsController(MovieRepository movieRepository, StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public void initialize() {
        setMovieCountByGenrePieChartData();
    }

    private void setMovieCountByGenrePieChartData() {
        var data = statisticsService.getMovieCountsByGenre();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.addAll(data.stream().map(pair -> new PieChart.Data(pair.getKey().getGenreName(), pair.getValue())).collect(Collectors.toSet()));
        movieCountByGenrePieChart.setData(pieChartData);
    }
}
