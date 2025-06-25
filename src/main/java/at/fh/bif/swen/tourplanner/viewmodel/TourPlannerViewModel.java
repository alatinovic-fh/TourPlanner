package at.fh.bif.swen.tourplanner.viewmodel;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.service.ReportService;
import at.fh.bif.swen.tourplanner.service.RouteService;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
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
    private final ManageTourViewModel manageTourViewModel;

    private Tour selectedTour;
    private final ObjectProperty<Tour> selectedTourProperty = new SimpleObjectProperty<>();

    public TourPlannerViewModel(TourPlannerService tourPlannerService, ManageTourViewModel manageTourViewModel, TourLogViewModel tourLogViewModel, ReportService reportService) {
        this.reportService = reportService;
        this.tourPlannerService = tourPlannerService;
        this.manageTourViewModel = manageTourViewModel;

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
        tourPlannerService.addTour(tour);
        allTours.add(tour);
        filterTours();
    }

    public void filterTours() {
        filteredTours.setAll(tourPlannerService.filterTours(allTours, searchQuery.get()));
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
        this.tourPlannerService.loadMap(tour);
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
}
