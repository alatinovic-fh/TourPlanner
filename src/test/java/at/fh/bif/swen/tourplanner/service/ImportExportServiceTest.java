package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TourLog;
import at.fh.bif.swen.tourplanner.persistence.entity.TransportType;
import at.fh.bif.swen.tourplanner.persistence.repository.TourLogRepository;
import at.fh.bif.swen.tourplanner.persistence.repository.TourRepository;
import at.fh.bif.swen.tourplanner.service.dto.TourExportDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImportExportServiceTest {

    @InjectMocks
    private ImportExportService importExportService;

    @Mock
    private TourRepository tourRepository;

    @Mock
    private TourLogRepository tourLogRepository;

    @TempDir
    Path tempDir;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Tour sampleTour;
    private List<TourLog> sampleLogs;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleTour = new Tour();
        sampleTour.setId(42); // Initial ID to verify it's reset on import
        sampleTour.setName("Test Tour");
        sampleTour.setDescription("Test Desc");
        sampleTour.setFromLocation("Vienna");
        sampleTour.setToLocation("Salzburg");
        sampleTour.setType(TransportType.CAR);

        TourLog log = new TourLog();
        log.setId(10);
        log.setComment("Nice trip");
        log.setRating(4);
        log.setDifficulty(3);
        log.setTotalDistance(12);
        log.setTotalTime(Duration.ofHours(2));
        sampleLogs = List.of(log);
    }

    @Test
    void exportTourToJson_shouldWriteFileWithTourData() throws IOException {
        File outputFile = tempDir.resolve("tour-export.json").toFile();

        when(tourLogRepository.findByTour(sampleTour)).thenReturn(sampleLogs);

        importExportService.exportTourToJson(outputFile.getAbsolutePath(), sampleTour);

        assertTrue(outputFile.exists());

        TourExportDTO result = objectMapper.readValue(outputFile, TourExportDTO.class);
        assertEquals("Test Tour", result.getTour().getName());
        assertEquals("Nice trip", result.getTourLogs().get(0).getComment());
    }

    @Test
    void importTourFromJson_shouldPersistTourAndLogs() throws IOException {
        File importFile = tempDir.resolve("import.json").toFile();

        TourExportDTO dto = new TourExportDTO(sampleTour, sampleLogs);
        objectMapper.writeValue(importFile, dto);

        Tour savedTour = new Tour();
        savedTour.setId(100);
        when(tourRepository.save(any(Tour.class))).thenReturn(savedTour);


        importExportService.importTourFromJson(importFile.getAbsolutePath());

        ArgumentCaptor<Tour> tourCaptor = ArgumentCaptor.forClass(Tour.class);
        verify(tourRepository).save(tourCaptor.capture());
        assertEquals(0, tourCaptor.getValue().getId()); // Should be reset before saving

        ArgumentCaptor<List<TourLog>> logsCaptor = ArgumentCaptor.forClass(List.class);
        verify(tourLogRepository).saveAll(logsCaptor.capture());
        List<TourLog> savedLogs = logsCaptor.getValue();

        assertEquals(0, savedLogs.get(0).getId()); // Reset
        assertEquals(savedTour, savedLogs.get(0).getTour()); // Set correct tour
    }
}
