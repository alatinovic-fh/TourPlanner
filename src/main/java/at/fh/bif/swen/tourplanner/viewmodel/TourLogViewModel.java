package at.fh.bif.swen.tourplanner.viewmodel;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TourLog;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import at.fh.bif.swen.tourplanner.util.IDGenerator;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.Duration;
import java.time.LocalDate;

public class TourLogViewModel {
    private final TourPlannerService service;

    ObservableList<TourLog> allTourLogs = FXCollections.observableArrayList();

    private final StringProperty comment = new SimpleStringProperty("");
    private final StringProperty difficulty = new SimpleStringProperty("");
    private final StringProperty totalDistance = new SimpleStringProperty("");
    private final StringProperty totalTime = new SimpleStringProperty("");
    private final BooleanProperty savedLog = new SimpleBooleanProperty(false);
    private final BooleanProperty deletedLog = new SimpleBooleanProperty(false);
    private final BooleanProperty addedLog = new SimpleBooleanProperty(false);

    private Tour selectedTour;
    private TourLog selectedTourLog;


    public TourLogViewModel(TourPlannerService service) {
        this.service = service;

    }

    public void addTourLog() {
        double totalDistanceDouble = 0;
        long totalTimeLong = 0;
        try{
            totalDistanceDouble = Double.parseDouble(totalDistance.get());
            totalTimeLong = Long.parseLong(totalTime.get());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        TourLog newTourLog = new TourLog(IDGenerator.nextTourLogId(), LocalDate.now(),comment.get(), difficulty.get(), totalDistanceDouble, Duration.ofMinutes(totalTimeLong), 1);
        allTourLogs.add(newTourLog);
        service.addTourLog(newTourLog, this.selectedTour);
        this.addedLog.set(true);
    }

    public void saveChanges() {
        double totalDistanceDouble = 0;
        long totalTimeLong = 0;
        try{
            totalDistanceDouble = Double.parseDouble(totalDistance.get());
            totalTimeLong = Long.parseLong(totalTime.get());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        TourLog tourLog = new TourLog(selectedTourLog.getId(), LocalDate.now(),comment.get(), difficulty.get(), totalDistanceDouble, Duration.ofMinutes(totalTimeLong), 1);
        this.selectedTourLog = tourLog;
        service.updateTourLog(this.selectedTourLog, this.selectedTour);
        savedLog.set(true);
    }

    public void deleteSelectedTourLog() {
        if (selectedTourLog != null) {
            service.deleteTourLog(this.selectedTourLog, this.selectedTour);
            deletedLog.set(true);
            selectedTourLog = null;
        }
    }

    public ObservableList<TourLog> reloadTourLogs() {
        if (this.service.loadTourLogs(this.selectedTour) != null) {
            this.allTourLogs.setAll(this.service.loadTourLogs(this.selectedTour));
        }
        return this.allTourLogs;
    }

    public void bindToSelectedTourProperty(ReadOnlyObjectProperty<Tour> selectedTourProperty) {
        // Not sure if I should connect the two VMs but I cant think of another way
        selectedTourProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                this.selectedTour = newVal;
                reloadTourLogs();
            }
        });
    }


    public StringProperty commentProperty() {
        return comment;
    }


    public StringProperty difficultyProperty() {
        return difficulty;
    }


    public StringProperty totalDistanceProperty() {
        return totalDistance;
    }

    public StringProperty totalTimeProperty() {
        return totalTime;
    }

    public BooleanProperty savedLogProperty() {
        return savedLog;
    }

    public BooleanProperty deletedLogProperty() {
        return deletedLog;
    }

    public BooleanProperty addedLogProperty() {
        return addedLog;
    }

    public void setSelectedTourLog(TourLog selectedTourLog) {

        //UPDATED
        comment.set(selectedTourLog.getComment());
        difficulty.set(selectedTourLog.getDifficulty());
        totalDistance.set(String.valueOf(selectedTourLog.getTotalDistance()));
        totalTime.set(String.valueOf(selectedTourLog.getTotalTime()));
        this.selectedTourLog = selectedTourLog;

    }


}
