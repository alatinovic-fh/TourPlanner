package at.fh.bif.swen.tourplanner.integration;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Duration;

public class TourMetricExtractor implements MetricExtractor {



    @Override
    public double getDistance(JsonNode jsonNode) {
        return jsonNode
                .get("features")
                .get(0)
                .get("properties")
                .get("segments")
                .get(0)
                .get("distance")
                .asDouble();
    }

    @Override
    public Duration getDuration(JsonNode jsonNode) {
        double durationInSeconds = jsonNode
                .get("features")
                .get(0)
                .get("properties")
                .get("segments")
                .get(0)
                .get("duration")
                .asDouble();

        return Duration.ofSeconds((long) durationInSeconds);
    }
}
