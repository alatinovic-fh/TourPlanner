package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TourLog;
import at.fh.bif.swen.tourplanner.persistence.repository.TourLogRepository;
import at.fh.bif.swen.tourplanner.persistence.repository.TourRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourPlannerService {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourLogRepository tourLogRepository;

    public TourPlannerService() {
    }

    public ObservableList<Tour> loadTours() {
        return FXCollections.observableArrayList(tourRepository.findAll());
    }

    public ObservableList<Tour> filterTours(ObservableList<Tour> tourList, String query) {
        return tourList.filtered(t -> t.getName().toLowerCase().contains(query.toLowerCase()));
    }

    public void addTour(Tour tour) {
        this.tourRepository.save(tour);
    }

    public void updateTour(Tour updatedTour) {
        if (updatedTour == null) {
            System.err.println("updateTour: updatedTour ist null!");
            return;
        }
        this.tourRepository.save(updatedTour);
    }

    public void deleteTour(Tour tour) {
        if (tour != null) {
            this.tourLogRepository.deleteAllByTour(tour);
            this.tourRepository.delete(tour);
        }
    }


    public ObservableList<TourLog> loadTourLogs(Tour selectedTour){
        if (selectedTour != null) {
            return FXCollections.observableArrayList(this.tourLogRepository.findByTour(selectedTour));
        }
        return null;
    }

    public void addTourLog(TourLog tourLog, Tour selectedTour) {
        if (tourLog != null) {
            tourLog.setTour(selectedTour);
            this.tourLogRepository.save(tourLog);
        }
    }

    public void updateTourLog(TourLog newTourLog) {
        if (newTourLog != null) {
            this.tourLogRepository.save(newTourLog);
        }
    }

    public void deleteTourLog(TourLog deletedTourLog, Tour selectedTour) {
        if (selectedTour != null) {
            this.tourLogRepository.delete(deletedTourLog);
        }
    }
}
