package at.fh.bif.swen.tourplanner.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DirectionalJSConverter {

    public static void exportJS(JsonNode node) {
        String filePath = "src/main/resources/static/route.js";
        ObjectMapper mapper = new ObjectMapper();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("var directions = ");
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
            writer.write(json);
            writer.write(";\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
