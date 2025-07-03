package at.fh.bif.swen.tourplanner.service;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import at.fh.bif.swen.tourplanner.persistence.entity.TourLog;
import at.fh.bif.swen.tourplanner.persistence.repository.TourLogRepository;
import at.fh.bif.swen.tourplanner.persistence.repository.TourRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class ReportService {

    @Autowired
    private TourLogRepository logRepository;

    @Autowired
    private TourRepository tourRepository;

    public void generateTourReport(String filename, Tour tour) throws Exception {
        PdfWriter writer = new PdfWriter(new FileOutputStream(filename));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Title
        Paragraph header = new Paragraph("Tour Report: " + tour.getName())
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
                .setFontSize(20)
                .setFontColor(ColorConstants.BLACK);
        document.add(header);

        // Tour Details Section
        document.add(new Paragraph("Description: " + tour.getDescription()).setFontSize(12));
        document.add(new Paragraph("From: " + tour.getFromLocation()).setFontSize(12));
        document.add(new Paragraph("To: " + tour.getToLocation()).setFontSize(12));
        document.add(new Paragraph("Type: " + tour.getType()).setFontSize(12));
        document.add(new Paragraph("Distance: " + tour.getDistance() + " km").setFontSize(12));
        document.add(new Paragraph("Estimated Time: " + formatDuration(tour.getEstimatedTime())).setFontSize(12));

        document.add(new Paragraph("\n"));

        // Tour Logs Table Header
        Paragraph logHeader = new Paragraph("Tour Logs")
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
                .setFontSize(16)
                .setFontColor(ColorConstants.RED);
        document.add(logHeader);

        // Tour Logs Table
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 2, 2, 2, 2}))
                .useAllAvailableWidth()
                .setFontSize(11);

        // Header Cells
        table.addHeaderCell(getHeaderCell("Date"));
        table.addHeaderCell(getHeaderCell("Comment"));
        table.addHeaderCell(getHeaderCell("Difficulty"));
        table.addHeaderCell(getHeaderCell("Distance (km)"));
        table.addHeaderCell(getHeaderCell("Time"));
        table.addHeaderCell(getHeaderCell("Rating"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Data Rows
        for (TourLog log : this.logRepository.findByTour(tour)) {
            table.addCell(log.getDate().format(dateFormatter));
            table.addCell(log.getComment());
            table.addCell(log.getDifficulty()+"");
            table.addCell(String.format("%.2f", log.getTotalDistance()));
            table.addCell(formatDuration(log.getTotalTime()));
            table.addCell(String.valueOf(log.getRating()));
        }

        document.add(table);

        document.close();
    }

    public void generateSummaryReport(String filename) throws Exception {
        List<Tour> tours = tourRepository.findAll();
        List<TourLog> allLogs = logRepository.findAll();

        PdfWriter writer = new PdfWriter(new FileOutputStream(filename));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph header = new Paragraph("Tour Summary Report")
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
                .setFontSize(20)
                .setFontColor(ColorConstants.GREEN);
        document.add(header);

        document.add(new Paragraph("This report summarizes average time, distance, and rating for each tour.")
                .setFontSize(12));
        document.add(new Paragraph("\n"));

        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 2, 2, 2}))
                .useAllAvailableWidth()
                .setFontSize(11);

        table.addHeaderCell(getHeaderCell("Tour Name"));
        table.addHeaderCell(getHeaderCell("Avg Time"));
        table.addHeaderCell(getHeaderCell("Avg Distance (km)"));
        table.addHeaderCell(getHeaderCell("Avg Rating"));

        for (Tour tour : tours) {
            List<TourLog> logs = allLogs.stream()
                    .filter(log -> log.getTour().getId() == tour.getId())
                    .toList();

            Duration avgTime = logs.stream()
                    .map(TourLog::getTotalTime)
                    .filter(d -> d != null)
                    .reduce(Duration::plus)
                    .map(d -> d.dividedBy(logs.size()))
                    .orElse(Duration.ZERO);

            double avgDistance = logs.stream()
                    .mapToDouble(TourLog::getTotalDistance)
                    .average().orElse(0.0);

            double avgRating = logs.stream()
                    .mapToDouble(TourLog::getRating)
                    .average().orElse(0.0);

            table.addCell(tour.getName());
            table.addCell(formatDuration(avgTime));
            table.addCell(String.format("%.2f", avgDistance));
            table.addCell(String.format("%.1f", avgRating));
        }

        document.add(table);
        document.close();
    }

    private Cell getHeaderCell(String text) throws Exception {
        return new Cell()
                .add(new Paragraph(text))
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBorder(new SolidBorder(ColorConstants.BLACK, 1));
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%dh %02dm", hours, minutes);
    }
}