package at.fh.bif.swen.tourplanner.view;

import at.fh.bif.swen.tourplanner.model.Tour;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

// TODO Add other UI elements according to Wireframe
// TODO create bindings to these elements
// TODO Add demo functionality better way to CRUD Tours
// TODO Add Tourlogs (CRUD)
// TODO Split ViewModel in different classes
//TODO Split Controller in different classes

public class TourPlanerController {
    @FXML
    public MenuItem exitMenuItem;

    @FXML
    public WebView mapView;

    @FXML
    public Label tourDataLabel;

    @FXML
    private javafx.scene.control.TextField searchField;

    @FXML
    private javafx.scene.control.ListView<Tour> tourListView;

    @FXML
    protected void onExitClick(ActionEvent actionEvent) {
        System.exit(0);
    }
    @FXML
    protected void onNewTourMenuItemClick() {
        viewModel.addTour();
    }

    @FXML
    protected void onNewTourLogClick(){
    }

    private final TourPlannerViewModel viewModel = new TourPlannerViewModel(new TourPlannerService());

    @FXML
    public void initialize() {
        //fixed prepared Leaflet Route
        WebEngine webEngine = mapView.getEngine();
        tourListView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            viewModel.setSelectedTour(newValue);
            URL url = null;
            try {
                url = new File(newValue.mapPath()).toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            webEngine.load(url.toString());
        });

        tourListView.setItems(viewModel.getFilteredTours());
        Bindings.bindBidirectional(searchField.textProperty(), viewModel.searchQueryProperty());
        Bindings.bindBidirectional(tourDataLabel.textProperty(), viewModel.tourDetailsProperty());
    }

}