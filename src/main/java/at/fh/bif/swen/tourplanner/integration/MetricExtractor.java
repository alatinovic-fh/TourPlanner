package at.fh.bif.swen.tourplanner.integration;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Duration;

public interface MetricExtractor {

    double getDistance(JsonNode jsonNode);

    Duration getDuration(JsonNode jsonNode);


}
