package at.fh.bif.swen.tourplanner.integration;

import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import at.fh.bif.swen.tourplanner.config.OpenRouteConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
//import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.util.Locale;

import java.net.URLEncoder;

@Slf4j
@Component
public class OpenRouteClient {

    private final OpenRouteConfig config;
    private final RestTemplate restTemplate;

    public OpenRouteClient(@Qualifier("openRouteConfig") OpenRouteConfig config) {
        this.config = config;
        this.restTemplate = new RestTemplate();
    }

    public GeoCoord geoCoord(String location){

        try{
            location = URLEncoder.encode(location,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("unsupported Characters in postal adress:");
            return null;
        }

        String url = String.format("https://api.openrouteservice.org/geocode/search?api_key=%s&text=%s", config.getApiKey(), location);

        try(HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString() );

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                try {
                    var coords = root.get("features").get(0).get("geometry").get("coordinates");
                    return new GeoCoord(coords.get(0).asDouble(), coords.get(1).asDouble());

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Failed to parse REST Response: " + e.getMessage());
                    return null;
                }

            }
            else {
                log.error("Failed to parse REST Response: " + response.body());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public JsonNode getRoute(TransportType type, GeoCoord start, GeoCoord end) {

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.UK);
        formatter.setMaximumFractionDigits(6);

        String url = String.format("https://api.openrouteservice.org/v2/directions/%s?api_key=%s&format=geojson&start=%s,%s&end=%s,%s",
                type.getProfile(), config.getApiKey(),
                formatter.format(start.latitude()),formatter.format(start.longitude()),
                formatter.format(end.latitude()),formatter.format(end.longitude()));

        try(HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder().
                    uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode route = mapper.readTree(response.body());
                return route;
            } else {
                log.error("Failed to parse REST Response: " + response.body());
                return null;
            }

        } catch (IOException | InterruptedException e) {
            log.error("Error getting Route");
            return null;
        }
    }
}
