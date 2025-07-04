package at.fh.bif.swen.tourplanner.viewmodel;

import at.fh.bif.swen.tourplanner.integration.exception.InvalidAddressException;
import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.service.ImportExportService;
import at.fh.bif.swen.tourplanner.service.ReportService;
import at.fh.bif.swen.tourplanner.service.RouteService;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import at.fh.bif.swen.tourplanner.service.exception.TourIncompleteException;
import javafx.application.HostServices;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class TourPlannerViewModel {
    private final ObservableList<Tour> allTours = FXCollections.observableArrayList();
    @Getter
    private final ObservableList<Tour> filteredTours = FXCollections.observableArrayList();
    private final StringProperty searchQuery = new SimpleStringProperty("");
    private final StringProperty tourDetails = new SimpleStringProperty("");

    private final TourPlannerService tourPlannerService;
    private final ReportService reportService;
    private final ImportExportService importExportService;

    private final ManageTourViewModel manageTourViewModel;

    private Tour selectedTour;
    private final ObjectProperty<Tour> selectedTourProperty = new SimpleObjectProperty<>();

    public TourPlannerViewModel(TourPlannerService tourPlannerService, ManageTourViewModel manageTourViewModel, TourLogViewModel tourLogViewModel, ReportService reportService, ImportExportService importExportService) {
        this.reportService = reportService;
        this.tourPlannerService = tourPlannerService;
        this.manageTourViewModel = manageTourViewModel;
        this.importExportService = importExportService;

        this.refreshTourList();
        this.searchQuery.addListener((obs, oldVal, newVal) -> filterTours());
        this.manageTourViewModel.savedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                refreshTourList();
                manageTourViewModel.savedProperty().set(false);
            }
        });
        manageTourViewModel.deletedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                refreshTourList();
                manageTourViewModel.deletedProperty().set(false); // reset
            }
        });
    }

    public void addTour(Tour tour) {
        try {
            tourPlannerService.addTour(tour);
            allTours.add(tour);
            filterTours();
        }catch (InvalidAddressException | TourIncompleteException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while adding new tour");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    public void filterTours() {
        filteredTours.setAll(tourPlannerService.filterTours(allTours, searchQuery.get()));
    }

    public void setSelectedTour(Tour tour) {
        this.selectedTour = tour;
        selectedTourProperty.set(tour);
        if (this.selectedTour != null) {
            this.tourDetailsProperty().set(this.selectedTour.details());
        } else {
            this.tourDetailsProperty().set("");
        }
    }

    public void refreshTourList() {
        allTours.setAll(tourPlannerService.loadTours());
        filterTours();
    }

    public void loadMap(Tour tour) {
        try {
            this.tourPlannerService.loadMap(tour);
        }catch (InvalidAddressException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while loading new tour");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    public void createTourReport(HostServices hostServices, boolean isSummary) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF Report");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Report", "*.pdf"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                if(isSummary) {
                    this.reportService.generateSummaryReport(file.getAbsolutePath());
                } else {
                    this.reportService.generateTourReport(file.getAbsolutePath(), this.selectedTour);
                }
                hostServices.showDocument(file.getAbsolutePath());
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while generating report.");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    public void exportTourData() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Tour Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tour-Data", "*.json"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                this.importExportService.exportTourToJson(file.getAbsolutePath(), this.selectedTour);
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while exporting tour-data.");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    public void importTourData() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Tour Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tour-Data", "*.json"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                this.importExportService.importTourFromJson(file.getAbsolutePath());
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while importing tour-data.");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
        this.refreshTourList();
    }

    public StringProperty searchQueryProperty() {
        return searchQuery;
    }

    public StringProperty tourDetailsProperty() {
        return tourDetails;
    }

    public ObjectProperty<Tour> selectedTourProperty() {
        return selectedTourProperty;
    }
}
