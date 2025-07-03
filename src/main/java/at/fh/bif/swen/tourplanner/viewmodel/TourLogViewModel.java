package at.fh.bif.swen.tourplanner.viewmodel;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TourLog;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;

@Component
public class TourLogViewModel {
    private final TourPlannerService service;

    ObservableList<TourLog> allTourLogs = FXCollections.observableArrayList();

    private final StringProperty comment = new SimpleStringProperty("");
    private final StringProperty difficulty = new SimpleStringProperty("");
    private final StringProperty totalDistance = new SimpleStringProperty("");
    private final StringProperty totalTime = new SimpleStringProperty("");
    private final StringProperty rating = new SimpleStringProperty("");
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
        int difficultyInteger = 0;
        long totalTimeLong = 0;
        int ratingInteger = 0;
        try{
            totalDistanceDouble = Double.parseDouble(totalDistance.get());
            totalTimeLong = Long.parseLong(totalTime.get());
            difficultyInteger = Integer.parseInt(difficulty.get());
            ratingInteger = Integer.parseInt(rating.get());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        TourLog newTourLog = new TourLog(LocalDate.now(),comment.get(), difficultyInteger, totalDistanceDouble, Duration.ofMinutes(totalTimeLong), ratingInteger);
        allTourLogs.add(newTourLog);
        service.addTourLog(newTourLog, this.selectedTour);
        service.calculateAttributes(this.selectedTour);
        this.addedLog.set(true);
    }

    public void saveChanges() {
        double totalDistanceDouble = 0;
        long totalTimeLong = 0;
        int difficultyInteger = 0;
        int ratingInteger = 0;
        try{
            totalDistanceDouble = Double.parseDouble(totalDistance.get());
            totalTimeLong = Long.parseLong(totalTime.get());
            difficultyInteger = Integer.parseInt(difficulty.get());
            ratingInteger = Integer.parseInt(rating.get());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        this.selectedTourLog.setTour(selectedTour);
        this.selectedTourLog.setDate(selectedTourLog.getDate());
        this.selectedTourLog.setComment(comment.get());
        this.selectedTourLog.setDifficulty(difficultyInteger);
        this.selectedTourLog.setTotalDistance(totalDistanceDouble);
        this.selectedTourLog.setTotalTime(Duration.ofMinutes(totalTimeLong));
        this.selectedTourLog.setRating(ratingInteger);

        service.calculateAttributes(selectedTour);
        service.updateTourLog(this.selectedTourLog);
        savedLog.set(true);
    }

    public void deleteSelectedTourLog() {
        if (selectedTourLog != null) {
            service.deleteTourLog(this.selectedTourLog, this.selectedTour);
            deletedLog.set(true);
            selectedTourLog = null;
            service.calculateAttributes(this.selectedTour);
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
    public StringProperty ratingProperty(){
        return rating;
    }

    public void setSelectedTourLog(TourLog selectedTourLog) {

        comment.set(selectedTourLog.getComment());
        difficulty.set(selectedTourLog.getDifficulty()+"");
        totalDistance.set(String.valueOf(selectedTourLog.getTotalDistance()));
        rating.set(String.valueOf(selectedTourLog.getRating()));

        //Parse Duration to String format
        Duration duration = selectedTourLog.getTotalTime();
        long minutes = duration.toMinutes();
        totalTime.set(String.valueOf(minutes));

        this.selectedTourLog = selectedTourLog;

    }


}
