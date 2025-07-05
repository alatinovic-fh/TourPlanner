package at.fh.bif.swen.tourplanner.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Slf4j
@Component
public class POIClient {

    @Value("${openroute.api-key}")
    private String apikey;

    public List<String> getPoi(GeoCoord coord) throws IOException, InterruptedException {
        double lon = coord.longitude();
        double lat = coord.latitude();

        double delta = 0.005; // ≈ 500m, je nach Region

        double minLon = lon - delta;
        double maxLon = lon + delta;
        double minLat = lat - delta;
        double maxLat = lat + delta;

        String json = String.format(Locale.US, """
        {
          "request": "pois",
          "geometry": {
            "bbox": [
              [%.6f, %.6f],
              [%.6f, %.6f]
            ],
            "geojson": {
              "type": "Point",
              "coordinates": [%.6f, %.6f]
            },
            "buffer": 200
          }
        }
        """, minLat, minLon, maxLat, maxLon, lat, lon);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openrouteservice.org/pois"))
                .header("Authorization", this.apikey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Status: ", response.statusCode());
        log.debug(response.body());
        return parse(response.body());
    }

    public List<String> parse(String responseJson) throws JsonProcessingException {
        List<String> results = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseJson);
        JsonNode features = root.path("features");

        for (JsonNode feature : features) {
            JsonNode properties = feature.path("properties");
            JsonNode categoryIds = properties.path("category_ids");

            if (categoryIds.isMissingNode() || categoryIds.size() == 0) continue;

            for (Iterator<Map.Entry<String, JsonNode>> it = categoryIds.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> entry = it.next();
                JsonNode category = entry.getValue();

                String categoryName = category.path("category_name").asText("(no category name)");
                String categoryGroup = category.path("category_group").asText("(no group)");

                JsonNode tags = properties.path("osm_tags");
                String name = tags.path("name").asText("(no name)");
                if( name.equals("(no name)")) continue;
                String website = tags.path("website").asText("(no website)");

                String output = String.format("Name: %s \n Website: %s \n Category: %s (%s)",
                        name, website, categoryName, categoryGroup);

                results.add(output);
            }
        }

        return results;
    }
}
