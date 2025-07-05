package at.fh.bif.swen.tourplanner.integration;

import at.fh.bif.swen.tourplanner.config.OpenRouteConfig;
import at.fh.bif.swen.tourplanner.integration.exception.InvalidAddressException;
import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class OpenRouteClientTest {

    private OpenRouteClient client;

    @Autowired
    private OpenRouteConfig openRouteConfig;

    @Autowired
    private OpenRouteClient openRouteClient;

    @Test
    void geoCoord_whenFHTWpostal_thenFHTWGeoCoords() throws InvalidAddressException {

        //ARRANGE
        String postalAddress = "Höchstädtplatz 6, 1200 Wien";

        // Act
        GeoCoord result =  openRouteClient.geoCoord(postalAddress);

        // Assert
        assertNotNull(result);
        assertEquals(16.378317,result.latitude(),0.0001 );
        assertEquals(48.238992,result.longitude(), 0.0001 );
    }


    @Test
    void geoCoord_whenZanonipostal_thenZanoniGeoCoords() throws InvalidAddressException {
        // Arrange
        String postalAddress = "Rotenturmstrasse 1, 1010 Wien";

        // Act
        GeoCoord result =  openRouteClient.geoCoord(postalAddress);

        // Assert
        assertNotNull(result);
        assertEquals( result.latitude(), 16.372924);
        assertEquals( result.longitude(), 48.209379);
    }

    @Test
    void givenTwoGeoCoord_whengetRoute_thenOK() {
        // Arrange
        GeoCoord fhtw = new GeoCoord(16.378317, 48.238992);
        GeoCoord zanoni = new GeoCoord(16.372924, 48.209379);

        // Act
        JsonNode result = null;
        try {
            result = openRouteClient.getRoute(TransportType.CAR,fhtw,zanoni);
        } catch (InvalidAddressException e) {
            System.out.println(e.getMessage());
        }

        // Assert
        assertNotNull(result);
        var bbox = result.get("bbox");
        assertNotNull(bbox);
        assertEquals( bbox.get(0).asDouble(), 16.371256);
        assertEquals( bbox.get(1).asDouble(), 48.20916);
        assertEquals( bbox.get(2).asDouble(), 16.383488);
        assertEquals( bbox.get(3).asDouble(), 48.238904);
    }
}

