package at.fh.bif.swen.tourplanner.view;

import at.fh.bif.swen.tourplanner.TourPlanerApplication;
import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

// TODO Make language consistent in Application !!!
// TODO Errorhandling

@Controller
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

    private final TourPlannerViewModel viewModel;

    public TourPlanerController(TourPlannerViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {

        tourListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                this.viewModel.loadMap(newValue);
                try {
                    File file = new File("src/main/resources/static/map.html");
                    URL url = file.toURI().toURL();
                    mapView.getEngine().load(url.toString()+"?v=" + System.currentTimeMillis());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                viewModel.setSelectedTour(newValue);
            }
        });

        tourListView.setItems(viewModel.getFilteredTours());
        Bindings.bindBidirectional(searchField.textProperty(), viewModel.searchQueryProperty());
        Bindings.bindBidirectional(tourDataLabel.textProperty(), viewModel.tourDetailsProperty());

    }

    public void onCreateTourReportClick(ActionEvent actionEvent) {
        BufferedImage tourimage = SwingFXUtils.fromFXImage(this.mapView.snapshot(null, null), null);
        this.viewModel.createTourReport(TourPlanerApplication.HOST_SERVICES, false, tourimage);
    }

    public void onCreateSummaryClick(ActionEvent actionEvent){
        this.viewModel.createTourReport(TourPlanerApplication.HOST_SERVICES, true, null);
    }

    public void onExportTourDataClick(ActionEvent actionEvent) {
        this.viewModel.exportTourData();
    }

    public void onImportTourDataClick(ActionEvent actionEvent) {
        this.viewModel.importTourData();
    }
}