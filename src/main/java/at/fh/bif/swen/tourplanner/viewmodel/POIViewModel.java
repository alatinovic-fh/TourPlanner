package at.fh.bif.swen.tourplanner.viewmodel;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.service.RouteService;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class POIViewModel {

    @Getter
    private final ObservableList<String> poi = FXCollections.observableArrayList();

    private final RouteService routeService;

    public POIViewModel(RouteService routeService) {
        this.routeService = routeService;
    }

    public void bindToSelectedTourProperty(ReadOnlyObjectProperty<Tour> selectedTourProperty) {
        // Not sure if I should connect the two VMs but I cant think of another way
        selectedTourProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                this.reloadPOI();
            }
        });
    }

    public void reloadPOI() {
        try {
            poi.setAll(this.routeService.loadPoi());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
