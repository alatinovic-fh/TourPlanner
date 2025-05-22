package at.fh.bif.swen.tourplanner.model;

public enum TransportType {
    CAR("car"),
    NONE("none"),
    TRAIN("train");

    private final String name;

    TransportType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
