package at.fh.bif.swen.tourplanner.persistence.entity;

public enum TransportType {
    CAR("driving-car"),
    ROADCYCLING("cycling-road"),
    MOUNTAINBIKE("cycling-mountain"),
    BIKE("cycling-regular"),
    HIKE("foot-hiking"),
    WALK("foot-walking"),
    WHEELCHAIR("wheelchair");

    private final String profile;

    TransportType(String profile) {
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }
}
