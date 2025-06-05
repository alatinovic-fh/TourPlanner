package at.fh.bif.swen.tourplanner.persistence.repository;

import at.fh.bif.swen.tourplanner.persistence.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TourLogRepository extends JpaRepository<Tour, Long> {
}
