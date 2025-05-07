package at.fh.bif.swen.tourplanner.viewmodel;

import at.fh.bif.swen.tourplanner.model.Tour;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TourPlannerViewModel {
    private final ObservableList<Tour> allTours = FXCollections.observableArrayList();
    private final ObservableList<Tour> filteredTours = FXCollections.observableArrayList();
    private final StringProperty searchQuery = new SimpleStringProperty("");

    public TourPlannerViewModel() {
        searchQuery.addListener((obs, oldVal, newVal) -> filterTours());
    }

    public void addTour(Tour tour) {
        allTours.add(tour);
        filterTours();
    }

    public void filterTours() {
        filteredTours.setAll(
                allTours.filtered(t -> t.name().toLowerCase().contains(searchQuery.get().toLowerCase()))
        );
    }

    public ObservableList<Tour> getFilteredTours() {
        return filteredTours;
    }

    public StringProperty searchQueryProperty() {
        return searchQuery;
    }
}
