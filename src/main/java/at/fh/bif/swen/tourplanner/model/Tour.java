package at.fh.bif.swen.tourplanner.model;

// TODO implement full Tour record

public record Tour(String name, String description, String from, String to){
    @Override
    public String toString() {
        return "Tour [name=" + name + ", description=" + description + ", from=" + from+ ", to=" + to + "]";
    }
}
