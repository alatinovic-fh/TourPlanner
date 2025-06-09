package at.fh.bif.swen.tourplanner.integration;

import at.fh.bif.swen.tourplanner.config.OpenRouteConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class OpenRouteClientTest {

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
        assertEquals( result.lat(), 16.378317);
        assertEquals( result.lon(), 48.238992);
    }


    @Test
    void getRoute() {
    }
}