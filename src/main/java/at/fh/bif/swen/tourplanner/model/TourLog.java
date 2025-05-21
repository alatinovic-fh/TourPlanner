package at.fh.bif.swen.tourplanner.model;

import java.time.Duration;
import java.time.LocalDate;

public record TourLog(long id, LocalDate date, String comment, String difficulty, double totalDistance, Duration totalTime, int rating) {

}