package at.fh.bif.swen.tourplanner.model;

// TODO implement full Tour record + Add route Information

import java.sql.Time;

public record Tour(String name, String description, String from, String to, TransportType type, double distance, Time estimatedTime){
    @Override
    public String toString() {
        return name;
    }
}
