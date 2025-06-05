package at.fh.bif.swen.tourplanner.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tourlogs")
public class TourLog {
  @Id
// QUESTION: Should we generate ID here or as we have had it in addTourLog()?
//    @GeneratedValue(stratagy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "comment")
    private String comment;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "totalDistance")
    private double totalDistance;

    @Column(name = "totalTime")
    private Duration totalTime;

    @Column(name = "rating")
    private double rating;


}

/*

public record TourLog(long id, LocalDate date, String comment, String difficulty, double totalDistance, Duration totalTime, int rating) {

}*/
