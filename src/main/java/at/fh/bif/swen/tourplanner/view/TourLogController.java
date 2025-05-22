package at.fh.bif.swen.tourplanner.view;

import at.fh.bif.swen.tourplanner.model.TourLog;
import at.fh.bif.swen.tourplanner.viewmodel.TourLogViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.Duration;

public class TourLogController {

    private final TourLogViewModel tourLogViewModel;
    private final TourPlannerViewModel viewModel;

    @FXML private TableView<TourLog> tourLogTable;

    @FXML private TextField commentField;
    @FXML private TextField difficultyField;
    @FXML private TextField distanceField;
    @FXML private TextField durationField;

    @FXML private TableColumn<TourLog, String> dateColumn;
    @FXML private TableColumn<TourLog, String> commentColumn;
    @FXML private TableColumn<TourLog, String> difficultyColumn;
    @FXML private TableColumn<TourLog, String> distanceColumn;
    @FXML private TableColumn<TourLog, String> durationColumn;
    @FXML private TableColumn<TourLog, String> ratingColumn;



    public TourLogController(TourLogViewModel tourLogViewModel, TourPlannerViewModel viewModel) {
        this.tourLogViewModel = tourLogViewModel;
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {
        commentField.textProperty().bindBidirectional(tourLogViewModel.commentProperty());
        difficultyField.textProperty().bindBidirectional(tourLogViewModel.difficultyProperty());
        distanceField.textProperty().bindBidirectional(tourLogViewModel.totalDistanceProperty());
        durationField.textProperty().bindBidirectional(tourLogViewModel.totalTimeProperty());
        tourLogViewModel.bindToSelectedTourProperty(viewModel.selectedTourProperty());

        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().date().toString()));
        commentColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().comment()));
        difficultyColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().difficulty()));
        distanceColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().totalDistance())));
        durationColumn.setCellValueFactory(data -> new SimpleStringProperty(formatDuration(data.getValue().totalTime())));
        ratingColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().rating())));


        this.tourLogViewModel.savedLogProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                tourLogTable.setItems(this.tourLogViewModel.reloadTourLogs());
                this.tourLogViewModel.savedLogProperty().set(false);
            }
        });
        this.tourLogViewModel.deletedLogProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                tourLogTable.setItems(this.tourLogViewModel.reloadTourLogs());
                this.tourLogViewModel.deletedLogProperty().set(false);
            }
        });
        this.tourLogViewModel.addedLogProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                tourLogTable.setItems(this.tourLogViewModel.reloadTourLogs());
                this.tourLogViewModel.addedLogProperty().set(false);
            }
        });

        tourLogTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                this.tourLogViewModel.setSelectedTourLog(newValue);
            }
        });
    }

    @FXML
    public void onAddLog() {
        tourLogViewModel.addTourLog();
    }

    @FXML
    public void onDeleteLog() {
        tourLogViewModel.deleteSelectedTourLog();
    }

    @FXML
    public void onSaveChanges() {
        tourLogViewModel.saveChanges();
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
