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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import at.fh.bif.swen.tourplanner.integration.OpenRouteClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TourPlannerService {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourLogRepository tourLogRepository
            ;
    @Autowired
    private OpenRouteClient openRouteClient;


    @Autowired
    public TourPlannerService() {
    }

    public ObservableList<Tour> loadTours() {
        return FXCollections.observableArrayList(tourRepository.findAll());
    }

    public ObservableList<Tour> filterTours(ObservableList<Tour> tourList, String query) {
        return tourList.filtered(t -> t.getName().toLowerCase().contains(query.toLowerCase()));
    }

    //GOAL: get Json for Leaflet From Integration.OpenRouteClient
    public JsonNode getRouteFromAdress(Tour tour) {

        try{

            GeoCoord start = openRouteClient.geoCoord(tour.getFrom_location());
            GeoCoord end = openRouteClient.geoCoord(tour.getTo_location());

            if(start == null || end == null) {
                System.err.println("ERROR: Failed to find address");

                if(start == null) System.err.println("ERROR:starting address not found");   //NOTE not actually needed but.... FLAG:|> notification Visualisation would be nice
                if(end == null) System.err.println("ERROR:destination address not found");  //NOTE not actually needed but.... FLAG:|> notification Visualisation would be nice
                return null;
            }

            System.out.printf("Route found from %s to %s \n", start, end); // NOTE not actually needed but.... FLAG:|> notification Visualisation would be nice

            return openRouteClient.getRoute(tour.getType(),start,end);

        }catch(Exception e){
            System.err.println("ERROR: while finding route" + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    //GOAL: save adjusted JsonFile to JavaScript file --> to be used for the Leaflet + with route
    public void saveJsonRoute(JsonNode routeJsonNode) {
        new DirectionalJSConverter().exportJS(routeJsonNode, "src/main/resources/static/route.js");
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
