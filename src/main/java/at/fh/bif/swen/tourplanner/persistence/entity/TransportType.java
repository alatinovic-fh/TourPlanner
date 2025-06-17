package at.fh.bif.swen.tourplanner.persistence.entity;

public enum TransportType {
    CAR("driving-car"),
ROADCYCLING("cycling-road"),
MOUNTAINBIKE("cycling-mountain"),
    BIKE("cycling-regular"),
    HIKE("foot-hiking"),
    WALK("foot-walking"),
WHEELCHAIR("wheelchair");
//NOTE:      Not included in OpenRouteService (=>  https://giscience.github.io/openrouteservice/run-instance/configuration/engine/profiles/#ors-engine-profiles)
//    -> to be deleted
//    NONE("none"),
//    TRAIN("driving-train"),

    private final String profile;

    TransportType(String profile) {
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }
}
