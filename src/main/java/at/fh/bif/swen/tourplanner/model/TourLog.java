package at.fh.bif.swen.tourplanner.model;

import java.sql.Time;
import java.time.LocalDate;

public record TourLog(LocalDate date, String comment, String difficulty, double totalDistance, Time totalTime, int rating) {

}