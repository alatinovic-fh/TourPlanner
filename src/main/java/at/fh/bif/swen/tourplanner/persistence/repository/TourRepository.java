package at.fh.bif.swen.tourplanner.persistence.repository;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository  extends JpaRepository<Tour, Long> {
}
