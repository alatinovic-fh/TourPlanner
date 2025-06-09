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
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.util.Locale;

import java.net.URLEncoder;

@Component
public class OpenRouteClient implements OpenRoute {

    private final OpenRouteConfig config;
    private final RestTemplate restTemplate;        //FIXME: RestTemplate not recognized

    public OpenRouteClient(OpenRouteConfig config) {

        this.config = config;
        this.restTemplate = new RestTemplate();
    }



    @Overrde
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


                ObjectMapper mapper = new ObjectMapper();           //FIXME: ObjectMapper not recognized
                JsonNode root = mapper.readTree(response.body());   //FIXME: JsonNode not recognized as well as .readTree

                try {
                    var coords = root.get("features").get(0).get("geometry").get("coordinates");    //FIXME: root.get not recognized
                    return new GeoCoord(coords.get(0).asDouble(), coords.get(1).asDouble());        //FIXME: .get(n) not recognized

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


    @Override
    public JsonNode getRoute(TransportType type, GeoCoord start, GeoCoord end) {
        return null;
    }
}
