package at.fh.bif.swen.tourplanner.view;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.Duration;
import at.fh.bif.swen.tourplanner.util.IDGenerator;
import org.springframework.stereotype.Component;

@Component
public class AddTourController {

    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML private ComboBox<TransportType> transportTypeCombo;

    private Tour newTour;

    @FXML
    public void initialize() {
        transportTypeCombo.setItems(FXCollections.observableArrayList(TransportType.values()));
    }

    @FXML
    private void onCancel() {
        newTour = null;
        closeWindow();
    }

    @FXML
    private void onSave() {
        newTour = new Tour(
                IDGenerator.nextId(),
                nameField.getText(),
                descriptionField.getText(),
                fromField.getText(),
                toField.getText(),
                transportTypeCombo.getValue(),
                4,
                Duration.ofMinutes(30),
                "src/main/resources/map.html"
        );
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    public Tour getCreatedTour() {
        return newTour;
    }
}
