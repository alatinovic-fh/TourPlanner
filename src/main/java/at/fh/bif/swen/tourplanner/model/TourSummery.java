package at.fh.bif.swen.tourplanner.model;

public class TourSummery {private final String name;
    private final double avgTime;
    private final double avgDistance;
    private final double avgRating;

    public TourSummary(String name, double avgTime, double avgDistance, double avgRating) {
        this.name = name;
        this.avgTime = avgTime;
        this.avgDistance = avgDistance;
        this.avgRating = avgRating;
    }

    public String getName() { return name; }
    public double getAvgTime() { return avgTime; }
    public double getAvgDistance() { return avgDistance; }
    public double getAvgRating() { return avgRating; }
}
