module at.fh.bif.swen.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens at.fh.bif.swen.tourplanner to javafx.fxml;
    exports at.fh.bif.swen.tourplanner;
}