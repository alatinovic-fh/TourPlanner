package at.fh.bif.swen.tourplanner;

import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import at.fh.bif.swen.tourplanner.view.ManageTourController;
import at.fh.bif.swen.tourplanner.view.TourLogController;
import at.fh.bif.swen.tourplanner.view.TourPlanerController;
import at.fh.bif.swen.tourplanner.viewmodel.ManageTourViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.TourLogViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//FLAG: Refactorization - point 5
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;


/**
 * The Application class was adapted to the class material
 *
 * TODO check future usage and adapt it
 *
 */
@SpringBootApplication
public class TourPlanerApplication extends Application {

    //FLAG: Refactorization- point 5 --> Spring context lifecycle integration for JavaFX - Spring Boot
    private ConfigurableApplicationContext springContext;
/*
    private final TourPlannerService tourPlannerService;

    private final TourPlannerViewModel viewModel;
    private final ManageTourViewModel manageTourViewModel;
    private final TourLogViewModel tourLogViewModel;*/
//QUESTION: THIS Constructor shouldn't be here anymore going forth with spring boot right?
/*    public TourPlanerApplication() {
        tourPlannerService = new TourPlannerService();
        tourLogViewModel = new TourLogViewModel(tourPlannerService);
        manageTourViewModel = new ManageTourViewModel(tourPlannerService);
        viewModel = new TourPlannerViewModel(tourPlannerService, manageTourViewModel, tourLogViewModel);
    }*/

//FLAG: Refactorization- point 5 -->
    @Override
    public void init(){
        springContext = new SpringApplicationBuilder(TourPlanerApplication.class).run(getParameters().getRaw().toArray(new String[0]));
    }


    @Override
    public void start(Stage stage) throws IOException {
//FLAG: Refactorization- point 5 -->
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TourPlanerView.fxml"));
//        Parent root = loadRootNode(viewModel, manageTourViewModel, tourLogViewModel);
        Parent root = loadRootNode(springContext);
        showStage(stage, root);
    }

/*    public static Parent loadRootNode(TourPlannerViewModel viewModel, ManageTourViewModel manageTourViewModel, TourLogViewModel tourLogViewModel) throws IOException {
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
    }*/
//FLAG: Refactorization- point 5 -->
    public static Parent loadRootNode(ConfigurableApplicationContext springContext) throws IOException {
        FXMLLoader loader = new FXMLLoader(TourPlanerApplication.class.getResource("tourplaner.fxml"));
        loader.setControllerFactory(springContext::getBean);
        return loader.load();
    }


    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
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