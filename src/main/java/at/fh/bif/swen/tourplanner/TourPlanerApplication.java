package at.fh.bif.swen.tourplanner;

import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import at.fh.bif.swen.tourplanner.view.ManageTourController;
import at.fh.bif.swen.tourplanner.view.TourLogController;
import at.fh.bif.swen.tourplanner.view.TourPlanerController;
import at.fh.bif.swen.tourplanner.viewmodel.ManageTourViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.TourLogViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * The Application class was adapted to the class material
 *
 * TODO check future usage and adapt it
 *
 */
public class TourPlanerApplication extends Application {

    private final TourPlannerService tourPlannerService;

    private final TourPlannerViewModel viewModel;
    private final ManageTourViewModel manageTourViewModel;
    private final TourLogViewModel tourLogViewModel;

    public TourPlanerApplication() {
        tourPlannerService = new TourPlannerService();
        tourLogViewModel = new TourLogViewModel(tourPlannerService);
        manageTourViewModel = new ManageTourViewModel(tourPlannerService);
        viewModel = new TourPlannerViewModel(tourPlannerService, manageTourViewModel, tourLogViewModel);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = loadRootNode(viewModel, manageTourViewModel, tourLogViewModel);
        showStage(stage, root);
    }

    public static Parent loadRootNode(TourPlannerViewModel viewModel, ManageTourViewModel manageTourViewModel, TourLogViewModel tourLogViewModel) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TourPlanerApplication.class.getResource("tourplaner.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> {
            if (controllerClass == TourPlanerController.class) {
                return new TourPlanerController(viewModel);
            } else if (controllerClass == ManageTourController.class) {
                return new ManageTourController(manageTourViewModel, viewModel);
            }else if (controllerClass == TourLogController.class) {
                return new TourLogController(tourLogViewModel, viewModel);
            } else {
                throw new IllegalArgumentException("Unknown controller class: " + controllerClass);
            }
        });

        return fxmlLoader.load();
    }

    public static void showStage(Stage stage, Parent root) {
        Scene scene = new Scene(root);
        stage.setTitle("TourPlanner");
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}