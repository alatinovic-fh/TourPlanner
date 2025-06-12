package at.fh.bif.swen.tourplanner.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;

public class DirectionalJSConverter {

    public void exportJS(JsonNode node, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write("var directions = ");
            fw.write(mapper.writeValueAsString(node));
            fw.write(";\n");
            System.out.println("Directions exported to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }
}
