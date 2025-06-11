package at.fh.bif.swen.tourplanner.integration;

import at.fh.bif.swen.tourplanner.config.OpenRouteConfig;
import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import org.junit.jupiter.api.BeforeEach;
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
    void geoCoord_whenFHTWpostal_thenFHTWGeoCoords() {

        //ARRANGE
        String postalAddress = "Höchstädtplatz 6, 1200 Wien";

        // Act
        GeoCoord result =  openRouteClient.geoCoord(postalAddress);

        // Assert
        assertNotNull(result);
        assertEquals(16.378317,result.lat(),0.0001 );
        assertEquals(48.238992,result.lon(), 0.0001 );
    }


    @Test
    void geoCoord_whenZanonipostal_thenZanoniGeoCoords() {
        // Arrange
        String postalAddress = "Rotenturmstrasse 1, 1010 Wien";

        // Act
        GeoCoord result =  openRouteClient.geoCoord(postalAddress);

        // Assert
        assertNotNull(result);
        assertEquals( result.lat(), 16.372924);
        assertEquals( result.lon(), 48.209379);
    }

    @Test
    void givenTwoGeoCoord_whengetRoute_thenOK() {
        // Arrange
        GeoCoord fhtw = new GeoCoord(16.378317, 48.238992);
        GeoCoord zanoni = new GeoCoord(16.372924, 48.209379);

        // Act
        var result = openRouteClient.getRoute(TransportType.CAR,fhtw,zanoni);

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

