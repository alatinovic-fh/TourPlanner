package at.fh.bif.swen.tourplanner.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;

public class DirectionalJSConverter {

    public void exportJS(JsonNode node, String filePath) {
        //NOTE: This does not convert to JS but rather prepare the content within ("var directions =" )
        ObjectMapper mapper = new ObjectMapper();
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write("var directions = ");
            mapper.writerWithDefaultPrettyPrinter().writeValue(fw, node);   // NOTE: Make it JsonFormat pretty
//            fw.write(mapper.writeValueAsString(node));                    // NOTE Should be also possible but for human eyes easiert to parse
            fw.write(";\n");
            System.out.println("Directions exported to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
