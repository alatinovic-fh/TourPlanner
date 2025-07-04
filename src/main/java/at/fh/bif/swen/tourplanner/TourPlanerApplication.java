package at.fh.bif.swen.tourplanner;

import at.fh.bif.swen.tourplanner.config.OpenRouteConfig;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import at.fh.bif.swen.tourplanner.view.ManageTourController;
import at.fh.bif.swen.tourplanner.view.TourLogController;
import at.fh.bif.swen.tourplanner.view.TourPlanerController;
import at.fh.bif.swen.tourplanner.viewmodel.ManageTourViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.TourLogViewModel;
import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import javafx.application.Application;
import javafx.application.HostServices;
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
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;


/**
 * The Application class was adapted to the class material
 *
 *
 */
@EnableConfigurationProperties(OpenRouteConfig.class)
@SpringBootApplication
public class TourPlanerApplication extends Application {

    private ConfigurableApplicationContext springContext;
    public static HostServices HOST_SERVICES;

    @Override
    public void init(){
        springContext = new SpringApplicationBuilder(TourPlanerApplication.class)
                .run(getParameters().getRaw().toArray(new String[0]));
    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TourPlanerView.fxml"));
        Parent root = loadRootNode(springContext);
        showStage(stage, root);

        HOST_SERVICES = getHostServices();
    }

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