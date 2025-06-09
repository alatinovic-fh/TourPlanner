package at.fh.bif.swen.tourplanner.integration;

import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import at.fh.bif.swen.tourplanner.config.OpenRouteConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
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

@Component
public class OpenRouteClient implements OpenRoute { //Class 'OpenRouteClient' must either be declared abstract or implement abstract method 'getRoute(TransportType, GeoCoord, GeoCoord)' in 'OpenRoute'

    private final OpenRouteConfig config;
    private final RestTemplate restTemplate;

    public OpenRouteClient(OpenRouteConfig config) {

        this.config = config;
        this.restTemplate = new RestTemplate();
    }



    @Override
    public GeoCoord geoCoord(String postalAddress){
        //TODO: from address to geoCoordinate

        try{
            postalAddress = URLEncoder.encode(postalAddress,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("unsupported Characters in postal adress:" + e.getMessage());
            return null;
        }

        String url = String.format("https://api.openrouteservice.org/geocode/search?api_key=%s&text=%s", config.getApiKey(), postalAddress);

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
                    System.err.println("Failed to parse REST Response: " + e.getMessage());
                    return null;
                }

            }
            else {
                System.err.println("Failed to parse REST Response: " + response.body());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }


    public JsonNode getRoute(TransportType type, GeoCoord start, GeoCoord end) {

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.UK);
        formatter.setMaximumFractionDigits(6);

        String url = String.format("https://api.openrouteservice.org/v2/directions/%s?api_key=%s&start=%s,%s&end=%s,%s",
                type.toString(), config.getApiKey(),
                formatter.format(start.lat()),formatter.format(start.lon()),
                formatter.format(end.lat()),formatter.format(end.lon()));

        try(HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder().
                    uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                System.out.println(root.toPrettyString());
                return root;
            } else {
                System.err.println("Failed to parse REST Response: " + response.body());
                return null;
            }

        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
