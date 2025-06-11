package at.fh.bif.swen.tourplanner.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component; //Question:

@Component
@ConfigurationProperties(prefix = "openroute")
public class OpenRouteConfig {

    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @PostConstruct
    public void debug() {
        System.out.println("[CONFIG] API Key laoded: "+apiKey);
    }
}
