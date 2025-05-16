package at.fh.bif.swen.tourplanner.model;

// TODO implement full Tour record

public record Tour(String name, String description, String from, String to, String imagePath){
    @Override
    public String toString() {
        return name;
    }
}
