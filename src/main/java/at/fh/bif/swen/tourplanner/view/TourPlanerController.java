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

public class TourPlanerController {
    @FXML
    public MenuItem exitMenuItem;

    @FXML
    public WebView mapView;

    @FXML
    private MenuItem newTourMenuItem;

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

    private final TourPlannerViewModel viewModel = new TourPlannerViewModel(new TourPlannerService());

    @FXML
    public void initialize() throws MalformedURLException {
        //Placeholder Leaflet Route
        WebEngine webEngine = mapView.getEngine();
        URL url = new File("src/main/resources/map.html").toURI().toURL();
        webEngine.load(url.toString());
        tourListView.setItems(viewModel.getFilteredTours());
        Bindings.bindBidirectional(searchField.textProperty(), viewModel.searchQueryProperty());
    }

}