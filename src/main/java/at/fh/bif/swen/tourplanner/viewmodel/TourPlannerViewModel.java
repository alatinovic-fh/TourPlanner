package at.fh.bif.swen.tourplanner.viewmodel;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class TourPlannerViewModel {
    private final ObservableList<Tour> allTours = FXCollections.observableArrayList();
    @Getter
    private final ObservableList<Tour> filteredTours = FXCollections.observableArrayList();
    private final StringProperty searchQuery = new SimpleStringProperty("");
    private final StringProperty tourDetails = new SimpleStringProperty("");

    private final TourPlannerService tourPlannerService;
    private final ManageTourViewModel manageTourViewModel;

    private Tour selectedTour;
    private final ObjectProperty<Tour> selectedTourProperty = new SimpleObjectProperty<>();

    public TourPlannerViewModel(TourPlannerService tourPlannerService, ManageTourViewModel manageTourViewModel, TourLogViewModel tourLogViewModel) {
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
        callOpenRouteAPI(tour);//GOAL: call API and if places found then save routeJson |-->

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



    //GOAL: --->| call OpenRoute
    public void callOpenRouteAPI(Tour tour) {
        JsonNode routeJson = tourPlannerService.getRouteFromAdress(tour);

        if (routeJson != null) {
            tourPlannerService.saveJsonRoute(routeJson);
        }else {
            System.err.println("No route found for " + tour);
        }
    }
}
