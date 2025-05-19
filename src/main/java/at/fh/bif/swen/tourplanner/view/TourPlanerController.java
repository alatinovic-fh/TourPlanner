package at.fh.bif.swen.tourplanner.view;

import at.fh.bif.swen.tourplanner.model.Tour;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import at.fh.bif.swen.tourplanner.viewmodel.ManageTourViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

// TODO Add Tourlogs (CRUD)
// TODO Make language consistent in Application !!!

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/fh/bif/swen/tourplanner/add_tour.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Neue Tour hinzufügen");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            AddTourController controller = loader.getController();
            Tour newTour = controller.getCreatedTour();

            if (newTour != null) {
                viewModel.addTour(newTour);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onNewTourLogClick(){
    }
    ;
    private final TourPlannerViewModel viewModel;

    public TourPlanerController(TourPlannerViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {
        //fixed prepared Leaflet Route
        // Not sure if this will be the right solution for in the future but for now it looks okay
        tourListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                URL url = null;
                try {
                    url = new File(newValue.mapPath()).toURI().toURL();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                mapView.getEngine().load(url.toString());
                viewModel.setSelectedTour(newValue);
            }
        });

        tourListView.setItems(viewModel.getFilteredTours());
        Bindings.bindBidirectional(searchField.textProperty(), viewModel.searchQueryProperty());
        Bindings.bindBidirectional(tourDataLabel.textProperty(), viewModel.tourDetailsProperty());

    }

}