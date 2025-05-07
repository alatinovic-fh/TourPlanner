package at.fh.bif.swen.tourplanner.view;

import at.fh.bif.swen.tourplanner.model.Tour;
import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

import java.util.List;

public class TourPlanerController {
    @FXML
    public MenuItem exitMenuItem;

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
        Tour tour = new Tour("Wien-Rundfahrt", "Eine Stadttour", "Wien", "Wien");
        Tour tour2 = new Tour("NÖ-Rundfahrt", "Eine NÖ Tour", "Korneuburg", "Melk");
        viewModel.addTour(tour);
        viewModel.addTour(tour2);
    }

    private final TourPlannerViewModel viewModel = new TourPlannerViewModel();

    @FXML
    public void initialize() {
        tourListView.setItems(viewModel.getFilteredTours());
        Bindings.bindBidirectional(searchField.textProperty(), viewModel.searchQueryProperty());
    }
}