package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TourLog;
import at.fh.bif.swen.tourplanner.persistence.repository.TourLogRepository;
import at.fh.bif.swen.tourplanner.persistence.repository.TourRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void calculateAttributes(Tour tour) {
        List<TourLog> tourLogs = this.tourLogRepository.findByTour(tour);

        // --- Popularity calculation ---
        int tourLogCount = (tourLogs == null) ? 0 : tourLogs.size();
        int popularity;

        if (tourLogCount <= 10) {
            popularity = 1;
        } else if (tourLogCount <= 15) {
            popularity = 2;
        } else if (tourLogCount <= 25) {
            popularity = 3;
        } else if (tourLogCount <= 40) {
            popularity = 4;
        } else {
            popularity = 5;
        }
        tour.setPopularity(popularity);

        // --- Child-friendliness ---
        if (tourLogs == null || tourLogs.isEmpty()) {
            tour.setChildFriendliness(0);
            return;
        }

        double totalDifficulty = 0;
        double totalTime = 0;
        double totalDistance = 0;

        for (TourLog log : tourLogs) {
            totalDifficulty += log.getDifficulty();
            totalTime += log.getTotalTime().toHours();
            totalDistance += log.getTotalDistance();
        }

        int count = tourLogs.size();
        double avgDifficulty = totalDifficulty / count;
        double avgTime = totalTime / count;
        double avgDistance = totalDistance / count;

        // Difficulty logic
        double difficultyScore = 0;
        if (avgDifficulty <=1) {
            difficultyScore = 1;
        } else if(avgDifficulty <=2){
            difficultyScore = 2;
        } else if(avgDifficulty <=3){
            difficultyScore = 3;
        } else if(avgDifficulty <=4){
            difficultyScore = 4;
        } else if(avgDifficulty <=5){
            difficultyScore = 5;
        }

        // Time logic
        int timeScore = 0;
        if (avgTime <= 4) {
            timeScore = 1;
        } else if (avgTime <= 6) {
            timeScore = 2;
        } else if (avgTime <= 8) {
            timeScore = 3;
        } else if (avgTime <= 10) {
            timeScore = 4;
        } else {
            timeScore = 5;
        }


        // Distance logic
        double distanceScore = 0;
        if (avgDistance <= 20) {
            distanceScore = 1;
        } else if (avgDistance <= 25) {
            distanceScore = 2;
        } else if (avgDistance <= 35) {
            distanceScore = 3;
        } else if (avgDistance <= 40) {
            distanceScore = 4;
        } else {
            distanceScore = 5;
        }

        // Final child-friendliness
        double childFriendliness = (difficultyScore+timeScore+distanceScore)/3;

        tour.setChildFriendliness(childFriendliness);
        this.updateTour(tour);
    }
}
