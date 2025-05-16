package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.model.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TourPlannerService {

    public ObservableList<Tour> filterTours(ObservableList<Tour> tourList, String query) {
        return tourList.filtered(t -> t.name().toLowerCase().contains(query.toLowerCase()));
    }

    public ObservableList<Tour> loadTours(){
        Tour tour = new Tour("Pöchlarnweg", "Eine Stadttour", "Matzleinsdorf", "Pöchlarn",  "" );
        Tour tour2 = new Tour("NÖ-Rundfahrt", "Eine NÖ Tour", "Korneuburg", "Melk", "");
        ObservableList<Tour> tours = FXCollections.observableArrayList();
        tours.add(tour);
        tours.add(tour2);
        return tours;
    }
}
