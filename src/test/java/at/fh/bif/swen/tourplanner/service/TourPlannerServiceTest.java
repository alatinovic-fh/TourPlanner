package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.integration.exception.InvalidAddressException;
import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TourLog;
import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import at.fh.bif.swen.tourplanner.persistence.repository.TourLogRepository;
import at.fh.bif.swen.tourplanner.persistence.repository.TourRepository;
import at.fh.bif.swen.tourplanner.service.exception.*;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourPlannerServiceTest {

    @InjectMocks
    private TourPlannerService tourPlannerService;

    @Mock
    private TourRepository tourRepository;

    @Mock
    private TourLogRepository tourLogRepository;

    @Mock
    private RouteService routeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadTours_shouldReturnAllTours() {
        List<Tour> tours = List.of(new Tour(), new Tour());
        when(tourRepository.findAll()).thenReturn(tours);

        ObservableList<Tour> result = tourPlannerService.loadTours();

        assertEquals(2, result.size());
    }

    @Test
    void filterTours_shouldFilterCorrectly() {
        Tour t1 = new Tour(); t1.setName("Vienna Walk");
        Tour t2 = new Tour(); t2.setName("Salzburg Tour");
        ObservableList<Tour> allTours = javafx.collections.FXCollections.observableArrayList(t1, t2);

        ObservableList<Tour> result = tourPlannerService.filterTours(allTours, "vienna");

        assertEquals(1, result.size());
        assertEquals("Vienna Walk", result.get(0).getName());
    }

    @Test
    void addTour_shouldThrowIfTourIsIncomplete() {
        Tour tour = new Tour(); // all fields null

        assertThrows(TourIncompleteException.class, () -> tourPlannerService.addTour(tour));
    }

    @Test
    void addTour_shouldInvokeRouteServiceAndSave() throws Exception {
        Tour tour = new Tour();
        tour.setDescription("Desc");
        tour.setName("Name");
        tour.setFromLocation("From");
        tour.setToLocation("To");
        tour.setType(TransportType.CAR);

        tourPlannerService.addTour(tour);

        verify(routeService).calculateRouteInfo(tour);
        verify(routeService).updateRoute(tour);
        verify(tourRepository).save(tour);
    }

    @Test
    void updateTour_shouldThrowIfIncomplete() {
        Tour tour = new Tour();

        assertThrows(TourIncompleteException.class, () -> tourPlannerService.updateTour(tour));
    }

    @Test
    void deleteTour_shouldThrowIfTourLogsExist() {
        Tour tour = new Tour();
        when(tourLogRepository.findByTour(tour)).thenReturn(List.of(new TourLog()));

        assertThrows(TourLogsNotEmptyException.class, () -> tourPlannerService.deleteTour(tour));
    }

    @Test
    void deleteTour_shouldDeleteIfNoTourLogs() throws Exception {
        Tour tour = new Tour();
        when(tourLogRepository.findByTour(tour)).thenReturn(List.of());

        tourPlannerService.deleteTour(tour);

        verify(tourRepository).delete(tour);
    }

    @Test
    void addTourLog_shouldThrowIfTourNull() {
        assertThrows(TourNotSelectedException.class, () -> tourPlannerService.addTourLog(new TourLog(), null));
    }

    @Test
    void addTourLog_shouldThrowIfInvalidData() {
        Tour tour = new Tour();
        TourLog log = new TourLog(); // all null

        assertThrows(TourLogCommentMissingException.class, () -> tourPlannerService.addTourLog(log, tour));
    }

    @Test
    void addTourLog_shouldSaveIfValid() throws Exception {
        Tour tour = new Tour();
        TourLog log = new TourLog();
        log.setComment("Nice");
        log.setRating(4);
        log.setDifficulty(3);
        log.setTotalDistance(20);
        log.setTotalTime(Duration.ofHours(2));

        tourPlannerService.addTourLog(log, tour);

        assertEquals(tour, log.getTour());
        verify(tourLogRepository).save(log);
    }

    @Test
    void calculateAttributes_shouldSetPopularityAndChildFriendliness() throws Exception {
        Tour tour = new Tour();

        TourLog log1 = new TourLog();
        log1.setDifficulty(1);
        log1.setTotalTime(Duration.ofHours(2));
        log1.setTotalDistance(10);

        TourLog log2 = new TourLog();
        log2.setDifficulty(2);
        log2.setTotalTime(Duration.ofHours(4));
        log2.setTotalDistance(30);

        when(tourLogRepository.findByTour(tour)).thenReturn(List.of(log1, log2));
        tourPlannerService.calculateAttributes(tour);

        assertEquals(1, tour.getPopularity()); // 2 logs => popularity 1
        assertTrue(tour.getChildFriendliness() > 0);
    }

    @Test
    void calculateAttributes_shouldHandleEmptyLogs() throws Exception {
        Tour tour = new Tour();
        when(tourLogRepository.findByTour(tour)).thenReturn(Collections.emptyList());

        tourPlannerService.calculateAttributes(tour);

        assertEquals(0, tour.getChildFriendliness());
    }

    @Test
    void filterTourLogs_shouldFilterCorrectly() {
        TourLog log1 = new TourLog(); log1.setComment("Great");
        TourLog log2 = new TourLog(); log2.setComment("Boring");
        ObservableList<TourLog> logs = javafx.collections.FXCollections.observableArrayList(log1, log2);

        ObservableList<TourLog> result = tourPlannerService.filterTourLogs(logs, "great");

        assertEquals(1, result.size());
    }

    @Test
    void loadTourLogs_shouldReturnCorrectList() {
        Tour tour = new Tour();
        List<TourLog> logs = List.of(new TourLog());
        when(tourLogRepository.findByTour(tour)).thenReturn(logs);

        ObservableList<TourLog> result = tourPlannerService.loadTourLogs(tour);

        assertEquals(1, result.size());
    }
}
