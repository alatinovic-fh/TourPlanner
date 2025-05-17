package at.fh.bif.swen.tourplanner.model;

// TODO implement full Tour record + Add route Information

import java.net.URL;
import java.time.Duration;

public record Tour(String name, String description, String from, String to, TransportType type, double distance, Duration estimatedTime, String mapPath){
    @Override
    public String toString() {
        return name;
    }

    public String details(){
        String details = String.format("Tour: %s\nFrom: %s\nTo: %s\nTransport type: %s\nDistance: %.2f km",
                name,
                from,
                to,
                type,
                distance,
                estimatedTime
        );
        return details;
    }
}
