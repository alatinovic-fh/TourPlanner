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

    @Column(name = "from_location")
    private String from_location;

    @Column(name = "to_location")
    private String to_location;

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
                from_location,
                to_location,
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
