/*package at.fh.bif.swen.tourplanner.test;*/

import at.fh.bif.swen.tourplanner.TourPlanerApplication;
import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
/*
import at.fh.bif.swen.tourplanner.persistence.repository.TourRepository;
import at.fh.bif.swen.tourplanner.service.ImportExportService;
import at.fh.bif.swen.tourplanner.service.ReportService;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import at.fh.bif.swen.tourplanner.view.ManageTourController;
import at.fh.bif.swen.tourplanner.view.TourPlanerController;
import at.fh.bif.swen.tourplanner.view.AddTourController;
import at.fh.bif.swen.tourplanner.viewmodel.TourLogViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.ManageTourViewModel;*/


import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.builder.SpringApplicationBuilder;           /*INFO: NEW */
import org.springframework.boot.WebApplicationType;                         /*INFO: NEW */
import org.springframework.context.ConfigurableApplicationContext;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.NodeQueryUtils.hasText;

@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TourPlanerApplicationTest {

    private ConfigurableApplicationContext context;
    private Parent root;

    @Start
    private void start(Stage stage) throws IOException {

        //Info: 1. Boot the Spring Context exactly as our Main() would
        context = new SpringApplicationBuilder(TourPlanerApplication.class)
                .web(WebApplicationType.NONE) //disable Tombat to avoid port conflicts
                .headless(false)    //ensures that JavaFX can run

                .run();

        //Info: 2. Load the FXML,  allowing Spring to inject all Autowired beans
        root = TourPlanerApplication.loadRootNode(context);

        //Info: 3. Show it
        TourPlanerApplication.showStage(stage, root);
    }

    @Test
    @Order(1)
    void whenAddTour_thenTourListIsNotEmpty(FxRobot robot) throws InterruptedException {
        // ARRANGE
        openNewTourDialog(robot);

        // ACT
        fillTourForm(robot, "UI name","UI descr", "Graz", "Linz");
        robot.clickOn("#transportTypeCombo").clickOn("CAR");
        robot.clickOn("#btnAddTour");
        WaitForAsyncUtils.waitForFxEvents();
        sleep( 2000);


        //ASSERT
        ListView<?> listView = robot.lookup("#tourListView").query();
        assertNotNull(listView);
        assertFalse(listView.getItems().isEmpty());
        assertTrue(listView.getItems().get(0) instanceof Tour);
    }


    @Test
    @Order(2)
    void whenUpdateTour_thenTourUpdatedFromTourList(FxRobot fxRobot) throws InterruptedException {
        /*GOAL: I want to choose a Tour and change it*/
        /*GOAL: find the tour with the name "UITest name" which was saved in the previeous test */

        //ARRANGE
        fxRobot.clickOn("#tourListView").clickOn("UI name");
        WaitForAsyncUtils.waitForFxEvents();


        //ACT
        fxRobot.clickOn("#ManageTourTab");
        WaitForAsyncUtils.waitForFxEvents();
        sleep( 1000);


        fillTourForm(fxRobot, "UITest name Changed","UITest description Changed", "Graz", "Linz");


        fxRobot.clickOn("#transportCombo").clickOn("CAR");
        fxRobot.clickOn("#btnSave_mngTour");
        WaitForAsyncUtils.waitForFxEvents();
        sleep( 1000);

        //ASSERT
        /*NOTE Look if the Tour has been changeed and updated.*/

        fxRobot.clickOn("#tourListView");
        fxRobot.clickOn("UITest name Changed");
        WaitForAsyncUtils.waitForFxEvents();
        sleep( 1000);
        System.out.println("Tour name changed to: " + fxRobot.lookup("#tourListView").queryAs(ListView.class).getSelectionModel().getSelectedItem().toString() + "");

        ListView<?> listView = fxRobot.lookup("#tourListView").query();
        boolean checkTourExists = listView.getItems().stream()
                        .filter(item -> item instanceof Tour)
                        .map(item -> ((Tour) item).getName())
                        .anyMatch(name -> name.equals("UITest name Changed"));

        assertTrue(checkTourExists);


    }


    @Test
    @Order(3)
    void whenDeleteTour_thenTourDeletedFromTourList(FxRobot r) throws InterruptedException {
        r.clickOn("#tourListView").clickOn("UITest name Changed");
        WaitForAsyncUtils.waitForFxEvents();
        sleep( 1000);

        //ACT
        r.clickOn("Manage Tour");
        WaitForAsyncUtils.waitForFxEvents();
        sleep( 1000);

        r.clickOn("#btndel_mngTour");
        WaitForAsyncUtils.waitForFxEvents();
        sleep( 1000);


        //ASSSERT
        ListView<?> listView = r.lookup("#tourListView").query();
        assertFalse(
            listView.getItems().stream()
                .map(item -> ((Tour) item).getName())
                .anyMatch(name -> name.equals("UITest name Changed")),
            "Deleted tour should no longer appear in the list"
        );
    }

    @Test
    @Order(4)
    void justClicking_aroundTheUI(FxRobot robot) throws InterruptedException {
        //Goal: click through the main functionalities of the App and check if the elements are loading properly

        sleep(1000);
        robot.clickOn("#tourListView");
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1000);

        robot.clickOn("#tourListView").clickOn("Graz"); //INFO: insert Name of existing Tour
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1000);

        //Info: double check that the webView has loaded
        WebView mapView = (WebView) robot.lookup("#mapView").query();
        Assertions.assertNotNull(mapView);
        WebEngine webEngine = mapView.getEngine();

        robot.interact(() -> {// Run the state assertion on the FX thread to avoid IllegalStateException
            Assertions.assertEquals(Worker.State.SUCCEEDED, webEngine.getLoadWorker().getState());
        });



        robot.clickOn("#btnReport");
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1000);

        assertAll("Report menu items",
                () -> org.testfx.api.FxAssert.verifyThat("#createTourReport .label", LabeledMatchers.hasText("Create Tour Report")),
                () -> org.testfx.api.FxAssert.verifyThat("#createSummaryReport .label", LabeledMatchers.hasText("Create Summary Report"))
        );


        //Note: Switch to Tabs
        robot.clickOn("#ManageTourLogTab");
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1000);


        assertAll("Manage Tour Tab buttons",
                () -> org.testfx.api.FxAssert.verifyThat("#btnAdd_mngTourLogs", hasText("Add Log")),
                () -> org.testfx.api.FxAssert.verifyThat("#btnSave_mngTourLogs", hasText("Save Changes")),
                () -> org.testfx.api.FxAssert.verifyThat("#btnDelete_mngTourLogs", hasText("Delete Log"))

        );

        robot.clickOn("#ManageTourTab");
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1000);


        assertAll("Manage Tour Tab buttons",
                () -> org.testfx.api.FxAssert.verifyThat("#btnSave_mngTour", hasText("Save")),
                () -> org.testfx.api.FxAssert.verifyThat("#btndel_mngTour", hasText("Delete Tour"))
        );


        robot.clickOn("General");
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1000);



        //Note: Exit
        robot.clickOn("#btnFileItem");
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1000);

        // Assert the File menu items' labels using descendant selector for the Label node
        assertAll("File menu items",
            () -> org.testfx.api.FxAssert.verifyThat("#newTourMenuItem .label", LabeledMatchers.hasText("New Tour")),
            () -> org.testfx.api.FxAssert.verifyThat("#exportTourData .label", LabeledMatchers.hasText("Export Tour")),
            () -> org.testfx.api.FxAssert.verifyThat("#importTourData .label", LabeledMatchers.hasText("Import Tour")),
            () -> org.testfx.api.FxAssert.verifyThat("#exitMenuItem .label", LabeledMatchers.hasText("Exit"))
        );

    }


//NOTE: Helper Methods:


    private void openNewTourDialog(FxRobot robot) throws InterruptedException {

        robot.clickOn("#btnFileItem");
        WaitForAsyncUtils.waitForFxEvents(); // stelle sicher, dass das Menü sichtbar ist

        robot.clickOn("#btnFileItem"); //NOTE: this second iteration is for mac users (YES, Macs need special treatments)
        WaitForAsyncUtils.waitForFxEvents(); // stelle sicher, dass das Menü sichtbar ist
        sleep(1000);

        robot.clickOn("#newTourMenuItem");
        WaitForAsyncUtils.waitForFxEvents();
    }

    private void fillTourForm(FxRobot robot, String name, String description, String from, String to) {


        replaceText(robot,"#nameField",name);

        replaceText(robot,"#descriptionField",description);

        replaceText(robot,"#fromField",from);

        replaceText(robot,"#toField",to);


    }

    private void replaceText(FxRobot robot, String fieldSelector, String newText) {

        try {
            WaitForAsyncUtils.waitForFxEvents();
            sleep(1300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // await().atMost(2,SECONDS).untilAsserted(() -> robot.lookup(fieldSelector).tryQuery().isPresent());
        // Click to focus the field
        robot.clickOn(fieldSelector);

        // Lookup the control
        TextInputControl input = robot.lookup(fieldSelector)
                                      .queryAs(TextInputControl.class);
        if (input == null) {
            throw new IllegalArgumentException("No text input control found for selector: " + fieldSelector);
        }

        // On the FX thread, select all existing text
        robot.interact(() -> {
            input.requestFocus();
            input.selectAll();
        });

        // Type the replacement text
        robot.write(newText);
    }




}

