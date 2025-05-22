module at.fh.bif.swen.tourplanner {
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.web;
    requires java.sql;

    opens at.fh.bif.swen.tourplanner to javafx.fxml;
    exports at.fh.bif.swen.tourplanner;
    exports at.fh.bif.swen.tourplanner.view;
    opens at.fh.bif.swen.tourplanner.view to javafx.fxml;
}