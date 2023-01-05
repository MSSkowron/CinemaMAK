package pl.edu.agh.cs.to.cinemamak.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.event.TablePerformanceChangeEvent;
import pl.edu.agh.cs.to.cinemamak.model.Movie;
import pl.edu.agh.cs.to.cinemamak.model.Performance;
import pl.edu.agh.cs.to.cinemamak.service.MovieService;
import pl.edu.agh.cs.to.cinemamak.service.PerformanceService;
import pl.edu.agh.cs.to.cinemamak.service.SessionService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@FxmlView("performance-view.fxml")
public class PerformanceController implements ApplicationListener<TablePerformanceChangeEvent> {

    public TextField titleTextField;
    public TextField directorTextField;
    public TextField yearTextField;
    public ChoiceBox<String> genreChoiceBox;
    public Button searchButton;
    public Button resetButton;
    @FXML
    private TableView<Performance> table;
    @FXML
    private TableColumn<Performance, String> columnTitle;
    @FXML
    private TableColumn<Performance, String> columnDate;
    @FXML
    private TableColumn<Performance, String> columnRoom;
    @FXML
    private TableColumn<Performance, String> columnSupervisor;
    @FXML
    private TableColumn<Performance, BigDecimal> columnPrice;
    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final SessionService sessionService;
    private final PerformanceService performanceService;
    private final MovieService movieService;
    private final FxWeaver fxWeaver;
    private Stage stage;

    public PerformanceController(MovieService movieService,PerformanceService performanceService, SessionService sessionService, FxWeaver fxWeaver){
        this.sessionService = sessionService;
        this.fxWeaver = fxWeaver;
        this.performanceService = performanceService;
        this.movieService = movieService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(){

        this.columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        this.columnDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Performance, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Performance, String> param) {
                if(param.getValue().getDate() != null) {
                    return new SimpleStringProperty(param.getValue().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                }
                else{
                    return new SimpleStringProperty("null");
                }
            }
        });

        this.columnTitle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Performance, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Performance, String> param) {
                if(param.getValue().getMovie() != null) {
                    return new SimpleStringProperty(param.getValue().getMovie().getTitle());
                }
                else{
                    return new SimpleStringProperty("null");
                }
            }
        });

        this.columnRoom.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Performance, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Performance, String> param) {
                if(param.getValue().getRoom() != null){
                    return new SimpleStringProperty(param.getValue().getRoom().getName());
                }
                else{
                    return new SimpleStringProperty("null");
                }
            }
        });

        this.columnSupervisor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Performance, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Performance, String> param) {
                if(param.getValue().getUser() != null){
                    return new SimpleStringProperty(param.getValue().getUser().getFirstName() + " " + param.getValue().getUser().getLastName());
                }
                else{
                    return new SimpleStringProperty("null");
                }
            }
        });
        movieService.getGenres().ifPresent(listM -> listM.forEach(genre -> this.genreChoiceBox.getItems().add(genre.getGenreName())));
        setPerformances(p -> true);

    }

    public void onMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            Stage newStage = new Stage();
            if(this.table.getSelectionModel().getSelectedItem() == null) return;
            fxWeaver.loadController(PerformanceEditController.class).setPerformance(this.table.getSelectionModel().getSelectedItem()).setStage(newStage);

            Scene newScene = new Scene(fxWeaver.loadView(PerformanceEditController.class));

            newStage.setTitle("Edit performance");
            newStage.setScene(newScene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(stage);

            newStage.show();
        }
    }

    public ObservableList<Performance> getPerformances(){
        ObservableList<Performance> performanceObservableList = FXCollections.observableArrayList();
        this.performanceService.getPerformances().ifPresent(performanceObservableList::addAll);
        return performanceObservableList;
    }

    public void setPerformances(Predicate<Performance> performancePredicate){
        FilteredList<Performance> filteredList = new FilteredList<>(getPerformances(), performancePredicate);
        SortedList<Performance> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(this.table.comparatorProperty());

        this.table.setItems(sortedList);
    }

    public void setAddButton(){
        Stage form = new Stage();
        fxWeaver.loadController(PerformanceFormController.class).setStage(form);
        Scene scene = new Scene(fxWeaver.loadView(PerformanceFormController.class));
        form.setScene(scene);
        form.setTitle("Add performance");
        form.initModality(Modality.WINDOW_MODAL);
        form.setAlwaysOnTop(true);
        form.initOwner(stage);
        form.show();
    }

    public void setDeleteButton(){
        Performance performance = this.table.getSelectionModel().getSelectedItem();
        this.performanceService.deletePerformanceById(performance.getId());
        applicationEventPublisher.publishEvent(new TablePerformanceChangeEvent(this));
    }

    @Override
    public void onApplicationEvent(TablePerformanceChangeEvent event) {
        setPerformances(p -> true);
        this.table.refresh();
    }

    public void onActionSearch(ActionEvent actionEvent) {

        String title = titleTextField.getText();
        String director = directorTextField.getText();
        String year = yearTextField.getText();
        String genre = genreChoiceBox.getValue();

        setPerformances(new Predicate<Performance>() {
            @Override
            public boolean test(Performance performance) {
                Movie movie = performance.getMovie();
                if(!title.equals("")){

                    Pattern pattern = Pattern.compile(title, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(movie.getTitle());
                    if(!matcher.find()){
                        return false;
                    }
                }
                if(!director.equals("")){

                    Pattern pattern = Pattern.compile(director, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(movie.getDirector());
                    if(!matcher.find()){
                        return false;
                    }
                }
                if(!year.equals("")) {

                    try {
                        if (!Integer.valueOf(movie.getDate().getYear()).equals(Integer.valueOf(year))) {
                            return false;
                        }
                    } catch(NumberFormatException exception){
                        showErrorDialog("Error occurred while filtering list of movies.",
                                "Year is not valid! Enter a number or left it empty.");
                    }
                }
                if(genre != null && !genre.equals("")){

                    if(!movie.getGenre().getGenreName().equals(genre)){
                        return false;
                    }
                }
                return true;
            }
        });

    }

    public void showErrorDialog(String header, String info){
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Error");
        dialog.setHeaderText(header);
        dialog.setContentText(info);
        dialog.show();
    }

    public void OnActionReset(ActionEvent actionEvent) {
        setPerformances(p -> true);
        this.genreChoiceBox.setValue("");
        this.directorTextField.setText("");
        this.titleTextField.setText("");
        this.yearTextField.setText("");
    }

}