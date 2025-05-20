package at.fh.bif.swen.tourplanner.test;

import at.fh.bif.swen.tourplanner.TourPlanerApplication;
import at.fh.bif.swen.tourplanner.model.Tour;
import at.fh.bif.swen.tourplanner.model.TransportType;
import at.fh.bif.swen.tourplanner.model.TourLog;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import at.fh.bif.swen.tourplanner.view.ManageTourController;
import at.fh.bif.swen.tourplanner.view.TourPlanerController;
import at.fh.bif.swen.tourplanner.view.AddTourController;
import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.ManageTourViewModel;


import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.util.NodeQueryUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class TourPlanerApplicationTest {

    private TourPlannerService tourPlannerService;
    private TourPlanerController tourPlanerController;
    private ManageTourController manageTourController;
    private ManageTourViewModel manageTourViewModel;
    private TourPlannerViewModel tourPlannerViewModel;
    private AddTourController addTourController;

    private Parent root;

    @Start
    private void start(Stage stage) throws IOException {
        tourPlannerService = new TourPlannerService();
        manageTourViewModel = new ManageTourViewModel(tourPlannerService);
        tourPlannerViewModel = new TourPlannerViewModel(tourPlannerService,manageTourViewModel);
        tourPlanerController = new TourPlanerController(tourPlannerViewModel);
        manageTourController = new ManageTourController(manageTourViewModel,tourPlannerViewModel);
        addTourController = new AddTourController();
        root = TourPlanerApplication.loadRootNode(tourPlannerViewModel,manageTourViewModel);
        TourPlanerApplication.showStage(stage, root);
    }

    @Test
    void whenAddTour_thenTourListIsNotEmpty(FxRobot robot) throws InterruptedException {
        // ARRANGE

        // ACT
        // Click on the File menu (by visible text, since Menu does not have fx:id)
        robot.clickOn("File");
        Thread.sleep(100);
        robot.clickOn("File"); // FIXME: if file is clicked once --> it will reload the whole stage???

        // Click on the New Tour menu item (ensure fx:id="newTourMenuItem" in FXML)
        robot.clickOn("#newTourMenuItem");
        Thread.sleep(700);
        // Fill out the form in the popup window
        robot.clickOn("#nameField").write("UITest name");

        robot.clickOn("#descriptionField").write("UITest description");

        robot.clickOn("#fromField").write("UITest from");

        robot.clickOn("#toField").write("UITest to");

        robot.clickOn("#transportTypeCombo").clickOn("CAR");
        // Click the Add/Save button (ensure fx:id="btnAddTour" in add_tour.fxml)
        robot.clickOn("#btnAddTour");

        // ASSERT
        // Wait so that we are back at the main window
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(1000);

        ListView<?> listView = robot.lookup("#tourListView").query();
        assertNotNull(listView);
        // Check list is not empty (use getItems().isEmpty(), not getSelectedItems())
        assertFalse(listView.getItems().isEmpty());
    }



}

