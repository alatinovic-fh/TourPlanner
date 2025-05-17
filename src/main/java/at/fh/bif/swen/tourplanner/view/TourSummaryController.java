package at.fh.bif.swen.tourplanner.view;
import at.fh.bif.swen.tourplanner.model.Tour;
import at.fh.bif.swen.tourplanner.model.TourSummery;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TourSummaryController {

    @FXML
    private TableView<TourSummary> summaryTable;

    @FXML
    private TableColumn<TourSummary, String> nameColumn;

    @FXML
    private TableColumn<TourSummary, Double> avgTimeColumn;

    @FXML
    private TableColumn<TourSummary, Double> avgDistanceColumn;

    @FXML
    private TableColumn<TourSummary, Double> avgRatingColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        avgTimeColumn.setCellValueFactory(new PropertyValueFactory<>("avgTime"));
        avgDistanceColumn.setCellValueFactory(new PropertyValueFactory<>("avgDistance"));
        avgRatingColumn.setCellValueFactory(new PropertyValueFactory<>("avgRating"));

        // TODO: Replace with real logic from TourLogs
        summaryTable.setItems(getMockSummaryData());
    }

    private ObservableList<TourSummary> getMockSummaryData() {
        return FXCollections.observableArrayList(
                new TourSummary("Pöchlarnweg", 45.0, 12.5, 4.2),
                new TourSummary("NÖ-Rundfahrt", 60.0, 35.0, 4.7)
        );
    }
}