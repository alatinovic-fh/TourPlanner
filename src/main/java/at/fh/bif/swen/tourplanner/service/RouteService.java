package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.integration.DirectionalJSConverter;
import at.fh.bif.swen.tourplanner.integration.GeoCoord;
import at.fh.bif.swen.tourplanner.integration.OpenRouteClient;
import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RouteService {

    @Autowired
    private OpenRouteClient openRouteClient;

    private GeoCoord startCoord;
    private GeoCoord endCoord;


    public void calculateRouteInfo(Tour tour) {
        try{
            this.updateCoords(tour);
            JsonNode routeJSON = this.getRouteJSON(tour);
            tour.setDistance(this.getDistance(routeJSON));
            tour.setEstimatedTime(this.getDuration(routeJSON));
        }catch (Exception e){
            System.err.println("Error calculating route info");
            e.printStackTrace();
        }
    }

    public void updateRoute(Tour tour){
        DirectionalJSConverter.exportJS(this.getRouteJSON(tour));
    }

    private void updateCoords(Tour tour) {
        this.startCoord = openRouteClient.geoCoord(tour.getFromLocation());
        this.endCoord = openRouteClient.geoCoord(tour.getToLocation());
        if (startCoord == null || endCoord == null) {
            System.err.println("ERROR: Failed to find address");
        }
    }

    private JsonNode getRouteJSON (Tour tour) {
        try{
            this.updateCoords(tour);
            JsonNode routeJSON = openRouteClient.getRoute(tour.getType(),this.startCoord,this.endCoord);
            return routeJSON;

        }catch(Exception e){
            System.err.println("ERROR: while finding route" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Duration getDuration(JsonNode route) {
        long duration = route
                .get("features").get(0)
                .get("properties")
                .get("segments").get(0)
                .get("duration").asLong();

        return Duration.ofSeconds(duration);
    }

    private long getDistance(JsonNode route) {
        long distance = route
                .get("features").get(0)
                .get("properties")
                .get("segments").get(0)
                .get("distance").asLong();

        return distance/1000;
    }
}
