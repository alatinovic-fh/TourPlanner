package at.fh.bif.swen.tourplanner.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "comment")
    private String comment;

    @Column(name = "difficulty")
    private int difficulty;

    @Column(name = "totalDistance")
    private double totalDistance;

    @Column(name = "totalTime")
    private Duration totalTime;

    @Column(name = "rating")
    private int rating;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "tour_id", nullable = false)
    @JsonIgnore
    private Tour tour;

    public TourLog(LocalDate date, String comment, int difficulty, double totalDistance, Duration totalTimeLong, int rating){
        this.date = date;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalDistance = totalDistance;
        this.totalTime = totalTimeLong;
        this.rating = rating;
    }
}
