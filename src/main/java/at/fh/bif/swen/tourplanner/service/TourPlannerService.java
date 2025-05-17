package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.model.Tour;
import at.fh.bif.swen.tourplanner.model.TransportType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.time.Duration;

public class TourPlannerService {

    public ObservableList<Tour> filterTours(ObservableList<Tour> tourList, String query) {
        return tourList.filtered(t -> t.name().toLowerCase().contains(query.toLowerCase()));
    }

    public ObservableList<Tour> loadTours(){
        Tour tour = new Tour("Pöchlarnweg", "Eine Stadttour", "Matzleinsdorf", "Pöchlarn", TransportType.CAR, 4, Duration.ofMinutes(15), "src/main/resources/map.html");
        Tour tour2 = new Tour("NÖ-Rundfahrt", "Eine NÖ Tour", "Korneuburg", "Melk", TransportType.CAR, 4, Duration.ofMinutes(50), "src/main/resources/map2.html");
        ObservableList<Tour> tours = FXCollections.observableArrayList();
        tours.add(tour);
        tours.add(tour2);
        return tours;
    }
}
