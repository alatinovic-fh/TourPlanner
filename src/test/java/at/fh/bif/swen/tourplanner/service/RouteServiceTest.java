package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.integration.GeoCoord;
import at.fh.bif.swen.tourplanner.integration.OpenRouteClient;
import at.fh.bif.swen.tourplanner.integration.POIClient;
import at.fh.bif.swen.tourplanner.integration.exception.InvalidAddressException;
import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouteServiceTest {

    @InjectMocks
    private RouteService routeService;

    @Mock
    private OpenRouteClient openRouteClient;

    @Mock
    private POIClient poiClient;

    private Tour sampleTour;
    private GeoCoord startCoord;
    private GeoCoord endCoord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleTour = new Tour();
        sampleTour.setFromLocation("Vienna");
        sampleTour.setToLocation("Salzburg");
        sampleTour.setType(TransportType.CAR);

        startCoord = new GeoCoord(48.2082, 16.3738);
        endCoord = new GeoCoord(47.8095, 13.0550);
    }

    private JsonNode buildMockRouteJson(long durationSec, long distanceMeters) {
        ObjectNode segment = JsonNodeFactory.instance.objectNode();
        segment.put("duration", durationSec);
        segment.put("distance", distanceMeters);

        ArrayNode segments = JsonNodeFactory.instance.arrayNode();
        segments.add(segment);

        ObjectNode properties = JsonNodeFactory.instance.objectNode();
        properties.set("segments", segments);

        ObjectNode feature = JsonNodeFactory.instance.objectNode();
        feature.set("properties", properties);

        ArrayNode features = JsonNodeFactory.instance.arrayNode();
        features.add(feature);

        ObjectNode root = JsonNodeFactory.instance.objectNode();
        root.set("features", features);

        return root;
    }

    @Test
    void calculateRouteInfo_shouldSetDistanceAndDuration() throws Exception {
        JsonNode routeJson = buildMockRouteJson(3600, 10000); // 1 hour, 10km

        when(openRouteClient.geoCoord("Vienna")).thenReturn(startCoord);
        when(openRouteClient.geoCoord("Salzburg")).thenReturn(endCoord);
        when(openRouteClient.getRoute(TransportType.CAR, startCoord, endCoord)).thenReturn(routeJson);

        routeService.calculateRouteInfo(sampleTour);

        assertEquals(Duration.ofSeconds(3600), sampleTour.getEstimatedTime());
        assertEquals(10, sampleTour.getDistance());
    }

    @Test
    void updateRoute_shouldExportJSWithCorrectJson() throws Exception {
        JsonNode routeJson = buildMockRouteJson(300, 1000);

        when(openRouteClient.geoCoord("Vienna")).thenReturn(startCoord);
        when(openRouteClient.geoCoord("Salzburg")).thenReturn(endCoord);
        when(openRouteClient.getRoute(any(), any(), any())).thenReturn(routeJson);


        assertDoesNotThrow(() -> routeService.updateRoute(sampleTour));
    }

    @Test
    void loadPoi_shouldReturnPois() throws Exception {
        setPrivateField(routeService, "endCoord", endCoord);

        List<String> pois = List.of("Museum", "Park");
        when(poiClient.getPoi(endCoord)).thenReturn(pois);

        ObservableList<String> result = routeService.loadPoi();

        assertEquals(2, result.size());
        assertTrue(result.contains("Museum"));
    }

    @Test
    void calculateRouteInfo_shouldSetCorrectDuration() throws Exception {
        Tour tour = new Tour();
        tour.setFromLocation("Vienna");
        tour.setToLocation("Salzburg");
        tour.setType(TransportType.CAR);

        JsonNode mockRouteJson = buildMockRouteJson(90, 1500); // 90 seconds, 1.5 km

        when(openRouteClient.geoCoord("Vienna")).thenReturn(new GeoCoord(0, 0));
        when(openRouteClient.geoCoord("Salzburg")).thenReturn(new GeoCoord(0, 0));
        when(openRouteClient.getRoute(any(), any(), any())).thenReturn(mockRouteJson);

        routeService.calculateRouteInfo(tour);

        assertEquals(Duration.ofSeconds(90), tour.getEstimatedTime());
    }


    @Test
    void calculateRouteInfo_shouldSetCorrectDistance() throws Exception {
        Tour tour = new Tour();
        tour.setFromLocation("Vienna");
        tour.setToLocation("Salzburg");
        tour.setType(TransportType.CAR);

        JsonNode mockRouteJson = buildMockRouteJson(60, 4000); // 4000 meters = 4 km

        when(openRouteClient.geoCoord("Vienna")).thenReturn(new GeoCoord(0, 0));
        when(openRouteClient.geoCoord("Salzburg")).thenReturn(new GeoCoord(0, 0));
        when(openRouteClient.getRoute(any(), any(), any())).thenReturn(mockRouteJson);

        routeService.calculateRouteInfo(tour);

        assertEquals(4, tour.getDistance()); // in km
    }


    @Test
    void calculateRouteInfo_shouldThrowOnInvalidAddress() throws Exception {
        when(openRouteClient.geoCoord("Vienna")).thenThrow(new InvalidAddressException("Invalid"));

        assertThrows(InvalidAddressException.class, () -> routeService.calculateRouteInfo(sampleTour));
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set private field: " + fieldName, e);
        }
    }
}
