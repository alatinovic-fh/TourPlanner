package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TourLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TourPlannerService {

    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    private final Map<Long, ObservableList<TourLog>> tourLogs = new HashMap<>();
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
        tourLogs.put(tour.id(), FXCollections.observableArrayList());
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


    public ObservableList<TourLog> loadTourLogs(Tour selectedTour){
        if (selectedTour != null) {
            return this.tourLogs.get(selectedTour.id());
        }
        return null;
    }

    public void addTourLog(TourLog tourLog, Tour selectedTour) {
        if (tourLog != null) {
            this.tourLogs.get(selectedTour.id()).add(tourLog);
        }
    }

    public void updateTourLog(TourLog newTourLog, Tour selectedTour) {
        List<TourLog> tourlog = this.tourLogs.get(selectedTour.id());
        if (newTourLog != null) {
            for (TourLog current : tourlog) {
                if (current.id() == newTourLog.id()) {
                    tourlog.set(tourlog.indexOf(current), newTourLog);
                }
            }
        }
    }

    public void deleteTourLog(TourLog deletedTourLog, Tour selectedTour) {
        if (selectedTour != null) {
        List<TourLog> tourlog = this.tourLogs.get(selectedTour.id());
            tourlog.removeIf(current -> current.id() == deletedTourLog.id());
        }
    }
}
