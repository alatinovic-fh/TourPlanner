package at.fh.bif.swen.tourplanner.viewmodel;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;

import javafx.beans.property.*;

import java.time.Duration;

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
        Tour tour = new Tour(selectedTour.id(), tourName.get(), tourDescription.get(), tourFrom.get(), tourTo.get(), tourTransportType.get(), 4, Duration.ofMinutes(30), "src/main/resources/map.html");
        this.selectedTour = tour;
        service.updateTour(selectedTour);
        saved.set(true);
    }

    public void deleteSelectedTour() {
        if (selectedTour != null) {
            service.deleteTour(selectedTour);
            deleted.set(true);
            selectedTour = null;
        }
    }

    private void loadFromTour(Tour tour) {
        tourName.set(tour.name());
        tourDescription.set(tour.description());
        tourFrom.set(tour.from());
        tourTo.set(tour.to());
        tourTransportType.set(tour.type());
    }
}
