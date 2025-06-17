//package at.fh.bif.swen.tourplanner.test;
//
//import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
//import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
//import at.fh.bif.swen.tourplanner.service.TourPlannerService;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import java.time.Duration;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//class TourPlannerServiceTest {
//
//    TourPlannerService tourPlannerService;
//
//
//
//
//    @BeforeEach
//    void setUp() {
//
//        tourPlannerService = new TourPlannerService();
//
//        //create an existing Tour
//        Tour tourExisting = new Tour(1,"Init Tour","the initial Tour that was there already", "Vienna", "Rom", TransportType.CAR, 1135, Duration.ofHours(12), "Path/to/the/MapDetails");
//        //Create a MockListofTour with the tourExisting
//
//        tourPlannerService.addTour(tourExisting);
//
//    }
//
//    @Test
//    void given_userInputsNewTour_when_AddTour_then_addNewDataToTourList() {
//        //ARRANGE
//        Tour newToAddTour = new Tour(2,"toAdd Tour 1","the new Tour that should be added", "Vienna", "Linz", TransportType.CAR, 183, Duration.ofHours(3), "Path/to/the/MapDetails");
//
//        //ACT
//
//        tourPlannerService.addTour(newToAddTour);
//        ObservableList<Tour> tourList = tourPlannerService.loadTours(); // this hast to be done differntly because this method is not yet finished
//        //ASSERT
//        assertEquals(2, tourList.size());
//        assertTrue(tourList.contains(newToAddTour));
//    }
//
//
//    @Test
//    void given_existingTour_when_updateTour_then_tourIsUpdated() {
//        //ARRANGE
//        Tour updated = new Tour(1, "Updated Name", "New Desc", "Vienna", "Berlin", TransportType.CAR, 999, Duration.ofHours(15), "new/path");
//        tourPlannerService.updateTour(updated);
//
//        //ACT
//        Tour result = tourPlannerService.loadTours().stream()
//                .filter(t -> t.id() == 1)
//                .findFirst()
//                .orElse(null);
//
//        //ASSERT
//        assertNotNull(result);
//        assertEquals("Updated Name", result.name());
//    }
//
//
//
///*
//    Question: Is this even needed to be tested? --> this will have to change in the near future as we will be connected with a DB anyways?
//    @Test
//    void given_existingTourisNULL_when_updateTour_then_ErrorMessage() {
//        //ARRANGE
//        //ACT
//        //ASSERT
//    }
//*/
//
//
//
//    @Test
//    void given_aTour_when_deleteTour_then_thatTourIsDeletedFromTourList() {
//        //ARRANGE
//        Tour newToAddTour = new Tour(2,"toAdd Tour 1","the new Tour that should be added", "Vienna", "Linz", TransportType.CAR, 183, Duration.ofHours(3), "Path/to/the/MapDetails");
//        tourPlannerService.addTour(newToAddTour);
//
//        //ACT
//        tourPlannerService.deleteTour(newToAddTour);
//
//        ObservableList<Tour> tourList = tourPlannerService.loadTours();
//
//
//        //ASSERT
//        assertFalse(tourList.contains(newToAddTour));
//    }
//
//
//    @Test
//    void given_tourList_when_filterToursWithQuery_then_returnMatchingTours() {
//        // ARRANGE
//        Tour tour1 = new Tour(1, "Vienna Tour", "Desc1", "A", "B", TransportType.CAR, 100, Duration.ofHours(1), "path1");
//        Tour tour2 = new Tour(2, "Salzburg Ride", "Desc2", "A", "B", TransportType.TRAIN, 200, Duration.ofHours(2), "path2");
//        Tour tour3 = new Tour(3, "vienna Night", "Desc3", "A", "B", TransportType.NONE, 300, Duration.ofHours(3), "path3");
//        ObservableList<Tour> tours = FXCollections.observableArrayList(tour1, tour2, tour3);
//
//        // ACT
//        ObservableList<Tour> filtered = tourPlannerService.filterTours(tours, "vienna");
//
//        // ASSERT
//        assertEquals(2, filtered.size());
//        assertTrue(filtered.contains(tour1));
//        assertTrue(filtered.contains(tour3));
//        assertFalse(filtered.contains(tour2));
//    }
//
//}