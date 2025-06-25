package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.integration.DirectionalJSConverter;
import at.fh.bif.swen.tourplanner.integration.GeoCoord;
import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TourLog;
import at.fh.bif.swen.tourplanner.persistence.repository.TourLogRepository;
import at.fh.bif.swen.tourplanner.persistence.repository.TourRepository;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import at.fh.bif.swen.tourplanner.integration.OpenRouteClient;

@Slf4j
@Data
@Service
public class TourPlannerService {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourLogRepository tourLogRepository;

    @Autowired
    private RouteService routeService;

    public TourPlannerService() {
    }


    public ObservableList<Tour> loadTours() {
        return FXCollections.observableArrayList(tourRepository.findAll());
    }

    public ObservableList<Tour> filterTours(ObservableList<Tour> tourList, String query) {
        return tourList.filtered(t -> t.getName().toLowerCase().contains(query.toLowerCase()));
    }

    public void addTour(Tour tour) {
        this.routeService.calculateRouteInfo(tour);
        this.routeService.updateRoute(tour);
        this.tourRepository.save(tour);
    }

    public void loadMap(Tour tour) {
        this.routeService.updateRoute(tour);
    }

    public void updateTour(Tour updatedTour) {
        if (updatedTour == null) {
            log.warn("updateTour: updatedTour ist null!");
            return;
        }
        this.routeService.calculateRouteInfo(updatedTour);
        this.routeService.updateRoute(updatedTour);
        this.tourRepository.save(updatedTour);
    }

    public void deleteTour(Tour tour) {
        if (tour != null) {
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
