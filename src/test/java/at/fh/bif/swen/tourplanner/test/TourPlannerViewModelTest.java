package at.fh.bif.swen.tourplanner.test;

import at.fh.bif.swen.tourplanner.model.Tour;
import at.fh.bif.swen.tourplanner.model.TransportType;
import at.fh.bif.swen.tourplanner.service.TourPlannerService;
import at.fh.bif.swen.tourplanner.viewmodel.ManageTourViewModel;

import at.fh.bif.swen.tourplanner.viewmodel.TourPlannerViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TourPlannerViewModelTest {

    private TourPlannerService mockTourPlannerService;
    private ManageTourViewModel mockManageTourViewModel;
    private TourPlannerViewModel mockViewModel;
    private Tour setTour;

    @BeforeEach
    void setUp() {
        mockTourPlannerService = mock(TourPlannerService.class);
        mockManageTourViewModel = mock(ManageTourViewModel.class);

        //Mock observable triggers
        when(mockManageTourViewModel.savedProperty()).thenReturn(new SimpleBooleanProperty(false));
        when(mockManageTourViewModel.deletedProperty()).thenReturn(new SimpleBooleanProperty(false));

        mockViewModel = new TourPlannerViewModel(mockTourPlannerService, mockManageTourViewModel);

        setTour = new Tour(1, "Bike Ride", "riding high", "Innsbruck", "Deutschlandsberg",
                TransportType.CAR, 112.0, Duration.ofHours(2), "/map/path.png");
    }


    @Test
    void given_aNewTour_when_AddTour_thenTourIsAddedAndFiltered() {
        when(mockTourPlannerService.filterTours(any(), anyString()))
                .thenReturn(FXCollections.observableArrayList(setTour));
        mockViewModel.addTour(setTour);

        ObservableList<Tour> sortedList = mockViewModel.getFilteredTours();
        assertEquals(1, sortedList.size());
        assertTrue(sortedList.contains(setTour));

        verify(mockTourPlannerService, times(1)).addTour(setTour);
    }



    @Test
    void given_searchQuery_when_searchQueryChanged_thenFilteredTourUpdated() {
        when(mockTourPlannerService.filterTours(any(), eq("Bike")))
                .thenReturn(FXCollections.observableArrayList(setTour));

        // Note: ensures non-null -> filtered Tours always return a valid list
        when(mockTourPlannerService.filterTours(any(), anyString()))
                .thenReturn(FXCollections.observableArrayList(setTour));


        mockViewModel.addTour(setTour);
        mockViewModel.searchQueryProperty().set("Bike");


        ObservableList<Tour> sortedList = mockViewModel.getFilteredTours();
        assertEquals(1, sortedList.size());
        assertEquals("Bike Ride", sortedList.get(0).name());


    }

    @Test
    void given_currentTourlist_when_RefreshTourList_then_AllToursAreReplaced() {
        when(mockTourPlannerService.filterTours(any(), anyString()))
                .thenReturn(FXCollections.observableArrayList(setTour));

        mockViewModel.addTour(setTour);


        ObservableList<Tour> sortedList = mockViewModel.getFilteredTours();
        assertEquals(1, sortedList.size());
        assertTrue(sortedList.contains(setTour));
        verify(mockTourPlannerService, times(1))
        .filterTours(any(), anyString());

    }



}

