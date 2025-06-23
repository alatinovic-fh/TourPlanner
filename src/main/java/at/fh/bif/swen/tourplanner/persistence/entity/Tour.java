package at.fh.bif.swen.tourplanner.persistence.entity;

import jakarta.persistence.*;
import java.time.Duration;

import lombok.*;

@Getter
@Setter
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

    @Column(name = "from_location")
    private String fromLocation;    //Changed: Lombok incorrectly processed from_Location --> fromLocation

    @Column(name = "to_location")
    private String toLocation;      //Changed: Lombok incorrectly processed: to_location --> toLocation

    @Column(name = "type")
    private TransportType type;

    @Column(name = "distance")
    private double distance;

    @Column(name = "estimatedTime")
    private Duration estimatedTime;



    public Tour(String name, String description, String fromLocation, String toLocation, TransportType type, double distance, Duration estimatedTime) {
        this.name = name;
        this.description = description;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.type = type;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
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
                fromLocation,
                toLocation,
                type,
                distance,
                hours,
                minutes
        );

    }

    @Override
    public String toString() {
        return name;
    }
}
