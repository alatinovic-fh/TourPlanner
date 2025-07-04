package at.fh.bif.swen.tourplanner.viewmodel;

import at.fh.bif.swen.tourplanner.integration.exception.InvalidAddressException;
import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;

import at.fh.bif.swen.tourplanner.service.exception.TourIncompleteException;
import at.fh.bif.swen.tourplanner.service.exception.TourLogsNotEmptyException;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ManageTourViewModel {

    private final TourPlannerService service;

    private final StringProperty tourName = new SimpleStringProperty();
    private final StringProperty tourDescription = new SimpleStringProperty();
    private final StringProperty tourFrom = new SimpleStringProperty();
    private final StringProperty tourTo = new SimpleStringProperty();
    private final ObjectProperty<TransportType> tourTransportType = new SimpleObjectProperty<>();
    private final BooleanProperty saved = new SimpleBooleanProperty(false);
    private final BooleanProperty deleted = new SimpleBooleanProperty(false);

    private Tour selectedTour;

    public ManageTourViewModel(TourPlannerService service) {
        this.service = service;
    }

    public StringProperty tourNameProperty() {
        return tourName;
    }

    public StringProperty tourDescriptionProperty() {
        return tourDescription;
    }

    public StringProperty tourFromProperty() {
        return tourFrom;
    }

    public StringProperty tourToProperty() {
        return tourTo;
    }

    public ObjectProperty<TransportType> tourTransportTypeProperty() {
        return tourTransportType;
    }

    public BooleanProperty savedProperty() {
        return saved;
    }

    public BooleanProperty deletedProperty() {
        return deleted;
    }

    public void bindToSelectedTourProperty(ReadOnlyObjectProperty<Tour> selectedTourProperty) {
        // Not sure if I should connect the two VMs but I cant think of another way
        selectedTourProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                this.selectedTour = newVal;
                loadFromTour(newVal);
            }
        });
    }

    public void saveChanges() {
        try {
            this.selectedTour.setDescription(tourDescription.get());
            this.selectedTour.setName(tourName.get());
            this.selectedTour.setFromLocation(tourFrom.get());
            this.selectedTour.setToLocation(tourTo.get());
            this.selectedTour.setEstimatedTime(Duration.ofMinutes(30));
            this.selectedTour.setType(tourTransportType.get());
            service.updateTour(selectedTour);
            saved.set(true);
        }catch(InvalidAddressException | TourIncompleteException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while changing tour.");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    public void deleteSelectedTour() {
        if (selectedTour != null) {
            try {
                service.deleteTour(selectedTour);
                deleted.set(true);
                selectedTour = null;
            }catch (TourLogsNotEmptyException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error while deleting tour.");
                alert.setContentText(e.getLocalizedMessage());
                alert.showAndWait();
            }
        }
    }

    private void loadFromTour(Tour tour) {
        tourName.set(tour.getName());
        tourDescription.set(tour.getDescription());
        tourFrom.set(tour.getFromLocation());
        tourTo.set(tour.getToLocation());
        tourTransportType.set(tour.getType());
    }
}
