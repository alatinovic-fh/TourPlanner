package at.fh.bif.swen.tourplanner.model;
import java.time.Duration;

public record Tour(long id, String name, String description, String from, String to, TransportType type, double distance, Duration estimatedTime, String mapPath){
    @Override
    public String toString() {
        return name;
    }

    public String details() {
        long hours = estimatedTime.toHours();
        long minutes = estimatedTime.toMinutes() % 60;

        return String.format("""
                Tour: %s
                Description: %s
                From: %s
                To: %s
                Transport type: %s
                Distance: %.2f km
                Estimated time: %dh %02dm
                """,
                name,
                description,
                from,
                to,
                type,
                distance,
                hours,
                minutes
        );
    }
}
