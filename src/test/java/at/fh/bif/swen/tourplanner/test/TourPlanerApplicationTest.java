//package at.fh.bif.swen.tourplanner.test;
//
//import at.fh.bif.swen.tourplanner.TourPlanerApplication;
//import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
//import at.fh.bif.swen.tourplanner.persistence.repository.TourRepository;
//import at.fh.bif.swen.tourplanner.service.TourPlannerService;
//import at.fh.bif.swen.tourplanner.view.ManageTourController;
//import at.fh.bif.swen.tourplanner.view.TourPlanerController;
//import at.fh.bif.swen.tourplanner.view.AddTourController;
//import at.fh.bif.swen.tourplanner.viewmodel.TourLogViewModel;
//import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
//import at.fh.bif.swen.tourplanner.viewmodel.ManageTourViewModel;
//
//
//import javafx.scene.Parent;
//import javafx.scene.control.ListView;
//import javafx.stage.Stage;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.testfx.api.FxRobot;
//import org.testfx.framework.junit5.ApplicationExtension;
//import org.testfx.framework.junit5.Start;
//import org.testfx.util.WaitForAsyncUtils;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(ApplicationExtension.class)
//class TourPlanerApplicationTest {
//
//    private TourPlannerService tourPlannerService;
//    private TourPlanerController tourPlanerController;
//    private ManageTourController manageTourController;
//    private ManageTourViewModel manageTourViewModel;
//    private TourPlannerViewModel tourPlannerViewModel;
//    private TourLogViewModel tourLogViewModel;
//    private AddTourController addTourController;
//
//    private Parent root;
//
//    @Start
//    private void start(Stage stage) throws IOException {
//        tourPlannerService = new TourPlannerService();
//        manageTourViewModel = new ManageTourViewModel(tourPlannerService);
//        tourLogViewModel = new TourLogViewModel(tourPlannerService);
//        tourPlannerViewModel = new TourPlannerViewModel(tourPlannerService,manageTourViewModel, tourLogViewModel);
//        tourPlanerController = new TourPlanerController(tourPlannerViewModel);
//        manageTourController = new ManageTourController(manageTourViewModel,tourPlannerViewModel);
//        addTourController = new AddTourController();
//        root = TourPlanerApplication.loadRootNode(tourPlannerViewModel,manageTourViewModel, tourLogViewModel);
//        TourPlanerApplication.showStage(stage, root);
//    }
//
//    @Test
//    void whenAddTour_thenTourListIsNotEmpty(FxRobot robot) throws InterruptedException {
//        // ARRANGE
//
//        // ACT
//        robot.clickOn("#btnFileItem");
//        WaitForAsyncUtils.waitForFxEvents(); // stelle sicher, dass das Menü sichtbar ist
//
//        robot.clickOn("#newTourMenuItem");
//        WaitForAsyncUtils.waitForFxEvents();
//
//
//
//        robot.clickOn("#nameField").write("UITest name");
//        robot.clickOn("#descriptionField").write("UITest description");
//        robot.clickOn("#fromField").write("UITest from");
//        robot.clickOn("#toField").write("UITest to");
//
//        robot.clickOn("#transportTypeCombo").clickOn("CAR");
//        robot.clickOn("#btnAddTour");
//
//        WaitForAsyncUtils.waitForFxEvents();
//
//        //ASSERT
//        ListView<?> listView = robot.lookup("#tourListView").query();
//        assertNotNull(listView);
//        assertFalse(listView.getItems().isEmpty());
//        assertTrue(listView.getItems().get(0) instanceof Tour);
//    }
//
//
//}
//
