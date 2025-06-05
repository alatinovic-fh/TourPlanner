package at.fh.bif.swen.tourplanner.view;

import at.fh.bif.swen.tourplanner.viewmodel.ManageTourViewModel;
import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;

import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

@Component
public class ManageTourController {

    private final ManageTourViewModel manageTourViewModel;
    private final TourPlannerViewModel viewModel; //rethink this in the future

    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML private ComboBox<TransportType> transportCombo;


    public ManageTourController(ManageTourViewModel manageTourViewModel, TourPlannerViewModel viewModel) {
        this.manageTourViewModel = manageTourViewModel;
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {
        transportCombo.setItems(FXCollections.observableArrayList(TransportType.values()));
        nameField.textProperty().bindBidirectional(manageTourViewModel.tourNameProperty());
        descriptionField.textProperty().bindBidirectional(manageTourViewModel.tourDescriptionProperty());
        fromField.textProperty().bindBidirectional(manageTourViewModel.tourFromProperty());
        toField.textProperty().bindBidirectional(manageTourViewModel.tourToProperty());
        transportCombo.valueProperty().bindBidirectional(manageTourViewModel.tourTransportTypeProperty());
        manageTourViewModel.bindToSelectedTourProperty(viewModel.selectedTourProperty());

    }

    @FXML
    private void onSaveTour() {
        manageTourViewModel.saveChanges();
    }

    @FXML
    private void onDeleteTour() {
        manageTourViewModel.deleteSelectedTour();
    }
}
