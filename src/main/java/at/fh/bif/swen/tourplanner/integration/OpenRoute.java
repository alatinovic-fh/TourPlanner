package at.fh.bif.swen.tourplanner.integration;


import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import com.fasterxml.jackson.databind.JsonNode;

public interface OpenRoute {
    GeoCoord geoCoord (String address);
    JsonNode getRoute(TransportType routeType, GeoCoord start, GeoCoord end);
}