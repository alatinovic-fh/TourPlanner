package at.fh.bif.swen.tourplanner.persistence.entity;

import jakarta.persistence.*;
import java.time.Duration;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tour")




public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "from")
    private String from;

    @Column(name = "to")
    private String to;

    @Column(name = "type")
    private TransportType type;

    @Column(name = "distance")
    private double distance;

    @Column(name = "estimatedTime")
    private Duration estimatedTime;

    @Column(name = "mapPath")
    private String mapPath;

    //TODO: seperation of Concern --> move out the string return
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
/*

public record Tours(long id, String name, String description, String from, String to, TransportType type, double distance, Duration estimatedTime, String mapPath){
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
}*/
