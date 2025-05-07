package at.fh.bif.swen.tourplanner.model;

public record Tour(String name, String description, String from, String to){
    @Override
    public String toString() {
        return "Tour [name=" + name + ", description=" + description + ", from=" + from+ ", to=" + to + "]";
    }
}

class TourAsClass{
    private String name;
    private String description;
    private String from;
    private String to;
    //TODO full Tour object


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Tour [name=" + name + ", description=" + description + ", from=" + from+ ", to=" + to + "]";
    }
}
