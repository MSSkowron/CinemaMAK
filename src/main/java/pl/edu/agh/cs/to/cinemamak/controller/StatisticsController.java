package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.service.StatisticsService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@FxmlView("statistics-view.fxml")
public class StatisticsController {
    private static class ReportInterval {
        private final LocalDate from;
        private final LocalDate to;

        public ReportInterval(LocalDate from, LocalDate to) {
            this.from = from;
            this.to = to;
        }

        public List<LocalDate> generateDays() {
            List<LocalDate> res = new ArrayList<>();
            for (var date = from.plusDays(0); date.isBefore(to) || date.isEqual(to); date = date.plusDays(1)) {
                res.add(date);
            }
            return res;
        }
    }
    @FXML
    private PieChart movieCountByGenrePieChart;

    @FXML
    private PieChart performanceCountByGenrePieChart;

    @FXML
    private VBox generalStatistics;

    @FXML
    private VBox detailedReports;

    @FXML
    private DatePicker reportDateFrom;

    @FXML
    private DatePicker reportDateTo;

    @FXML
    private Button generateReportsButton;

    private final StatisticsService statisticsService;

    private final BooleanProperty generalStatisticsVisible = new SimpleBooleanProperty(true);
    private final ObjectProperty<Optional<ReportInterval>> selectedInterval = new SimpleObjectProperty<>(Optional.empty());

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public void initialize() {
        generalStatistics.visibleProperty().bind(generalStatisticsVisible);
        generalStatistics.managedProperty().bind(generalStatisticsVisible);

        detailedReports.visibleProperty().bind(generalStatisticsVisible.not());
        detailedReports.managedProperty().bind(generalStatisticsVisible.not());

        generateReportsButton.disableProperty()
                .bind(Bindings.createBooleanBinding(() -> reportDateFrom.getValue() == null ||
                            reportDateTo.getValue() == null ||
                            reportDateFrom.getValue().isAfter(reportDateTo.getValue()),
                        reportDateFrom.valueProperty(),
                        reportDateTo.valueProperty()));

        setMovieCountByGenrePieChartData();
        setPerformanceCountByGenrePieChartData();
    }

    private void setMovieCountByGenrePieChartData() {
        var data = statisticsService.getMovieCountsByGenre();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.addAll(data
                .stream()
                .map(pair -> new PieChart.Data(pair.getKey().getGenreName(), pair.getValue()))
                .collect(Collectors.toSet()));
        movieCountByGenrePieChart.setData(pieChartData);
    }

    private void setPerformanceCountByGenrePieChartData() {
        var data = statisticsService.getPlannedPerformanceCountsByGenre();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.addAll(data
                .stream()
                .map(pair -> new PieChart.Data(pair.getKey().getGenreName(), pair.getValue()))
                .collect(Collectors.toSet()));
        performanceCountByGenrePieChart.setData(pieChartData);
    }

    @FXML
    private void showGeneralStatistics() {
        generalStatisticsVisible.setValue(true);
    }

    @FXML
    private void showDetailedReports() {
        generalStatisticsVisible.setValue(false);
    }

    @FXML
    private void setReportInterval() {
        var from = reportDateFrom.getValue();
        var to = reportDateTo.getValue();
        selectedInterval.setValue(Optional.of(new ReportInterval(from, to)));
    }
}
