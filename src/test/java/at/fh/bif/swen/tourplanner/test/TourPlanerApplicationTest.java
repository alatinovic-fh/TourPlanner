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
        robot.moveTo("File").clickOn();
        WaitForAsyncUtils.waitForFxEvents(); // stelle sicher, dass das Menü sichtbar ist

        robot.clickOn("#newTourMenuItem");
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("#nameField").write("UITest name");
        robot.clickOn("#descriptionField").write("UITest description");
        robot.clickOn("#fromField").write("UITest from");
        robot.clickOn("#toField").write("UITest to");

        robot.clickOn("#transportTypeCombo").clickOn("CAR");
        robot.clickOn("#btnAddTour");

        WaitForAsyncUtils.waitForFxEvents();

        //ASSERT
        ListView<?> listView = robot.lookup("#tourListView").query();
        assertNotNull(listView);
        assertFalse(listView.getItems().isEmpty());
        assertTrue(listView.getItems().get(0) instanceof Tour);
    }


}

