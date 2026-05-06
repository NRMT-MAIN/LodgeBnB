package com.example.LodgeBnB.repositories.writes;

import com.example.LodgeBnB.models.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityWriteRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByAirbnbId(Long airbnbId);

    List<Availability> findByBookingId(Long bookingId);

    List<Availability> findByAirbnbIdAndDateBetween(Long airbnbId, LocalDate startDate, LocalDate endDate);

    Long countByAirbnbIdAndDateBetweenAndBookingIdIsNotNull(Long airbnbId, LocalDate startDate, LocalDate endDate);
}
