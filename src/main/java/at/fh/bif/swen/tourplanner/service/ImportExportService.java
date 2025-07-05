package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TourLog;
import at.fh.bif.swen.tourplanner.persistence.repository.TourLogRepository;
import at.fh.bif.swen.tourplanner.persistence.repository.TourRepository;
import at.fh.bif.swen.tourplanner.service.dto.TourExportDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ImportExportService {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourLogRepository tourLogRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public void exportTourToJson(String filePath, Tour tour) throws IOException {
        List<TourLog> logs = tourLogRepository.findByTour(tour);
        TourExportDTO dto = new TourExportDTO(tour, logs);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), dto);
    }

    public void importTourFromJson(String filePath) throws IOException {
        TourExportDTO dto = objectMapper.readValue(new File(filePath), TourExportDTO.class);
        dto.getTour().setId(0); // Added so Hibernate does not attempt update
        Tour savedTour = tourRepository.save(dto.getTour());
        for (TourLog log : dto.getTourLogs()) {
            log.setId(0); // Added so Hibernate does not attempt update
            log.setTour(savedTour);
        }
        tourLogRepository.saveAll(dto.getTourLogs());
    }
}