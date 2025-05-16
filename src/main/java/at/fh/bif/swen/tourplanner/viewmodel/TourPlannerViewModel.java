package at.fh.bif.swen.tourplanner.viewmodel;

import at.fh.bif.swen.tourplanner.model.Tour;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class TourPlannerViewModel {
    private final ObservableList<Tour> allTours = FXCollections.observableArrayList();
    private final ObservableList<Tour> filteredTours = FXCollections.observableArrayList();
    private final StringProperty searchQuery = new SimpleStringProperty("");
    private TourPlannerService tourPlannerService;


    public TourPlannerViewModel(TourPlannerService service) {
        this.tourPlannerService = service;
        this.searchQuery.addListener((obs, oldVal, newVal) -> filterTours());
    }

    public void addTour() {
        allTours.addAll(this.tourPlannerService.loadTours());
        filterTours();
    }

    public void filterTours() {
        filteredTours.setAll(tourPlannerService.filterTours(allTours, searchQuery.get()));
    }

    public ObservableList<Tour> getFilteredTours() {
        return filteredTours;
    }

    public StringProperty searchQueryProperty() {
        return searchQuery;
    }
}
