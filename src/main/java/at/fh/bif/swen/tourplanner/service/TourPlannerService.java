package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.model.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TourPlannerService {

    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    public TourPlannerService() {
    }

    public ObservableList<Tour> loadTours() {
        // TODO in future implement fetching data from database
        return tours;
    }

    public ObservableList<Tour> filterTours(ObservableList<Tour> tourList, String query) {
        return tourList.filtered(t -> t.name().toLowerCase().contains(query.toLowerCase()));
    }

    public void addTour(Tour tour) {
        tours.add(tour);
    }

    public void updateTour(Tour updatedTour) {
        if (updatedTour == null) {
            System.err.println("updateTour: updatedTour ist null!");
            return;
        }
        for (Tour current : tours) {
            if (current.id() == updatedTour.id()) {
                tours.set(tours.indexOf(current), updatedTour);
            }
        }
    }

    public void deleteTour(Tour tour) {
        if (tour != null) {
            tours.remove(tour);
        }
    }
}
