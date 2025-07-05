package at.fh.bif.swen.tourplanner.view;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.viewmodel.POIViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.TourLogViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;

import javax.swing.text.html.ListView;

@Controller
public class POIController {

    private final POIViewModel poiViewModel;
    private final TourPlannerViewModel viewModel;
    @FXML private javafx.scene.control.ListView<String> poiListView;

    public POIController(POIViewModel tourLogViewModel, TourPlannerViewModel viewModel) {
        this.poiViewModel = tourLogViewModel;
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {
        poiListView.setItems(this.poiViewModel.getPoi());
        poiViewModel.bindToSelectedTourProperty(viewModel.selectedTourProperty());
    }
}
