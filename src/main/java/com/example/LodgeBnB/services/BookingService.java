package com.example.LodgeBnB.services;

import com.example.LodgeBnB.dtos.CreateBookingRequest;
import com.example.LodgeBnB.dtos.UpdateBookingRequest;
import com.example.LodgeBnB.models.Airbnb;
import com.example.LodgeBnB.models.Availability;
import com.example.LodgeBnB.models.Booking;
import com.example.LodgeBnB.repositories.reads.RedisWriteRepository;
import com.example.LodgeBnB.repositories.writes.AirbnbWriteRepository;
import com.example.LodgeBnB.repositories.writes.AvailabilityWriteRepository;
import com.example.LodgeBnB.repositories.writes.BookingWriteRepository;
import com.example.LodgeBnB.services.concurrency.ConcurrencyControlStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService implements IBookingService {
    private final BookingWriteRepository bookingWriteRepository;
    private final AvailabilityWriteRepository availabilityWriteRepository;
    private final AirbnbWriteRepository airbnbWriteRepository;
    private final ConcurrencyControlStrategy concurrencyControlStrategy;
    private final RedisWriteRepository redisWriteRepository;

    @Override
    @Transactional
    public Booking createBooking(CreateBookingRequest request) {
        Airbnb airbnb = airbnbWriteRepository.findById(request.getAirbnbId())
                .orElseThrow(() -> new RuntimeException("Airbnb not found with ID: " + request.getAirbnbId()));

        if(request.getCheckInDate().isAfter(request.getCheckOutDate())) {
            throw new RuntimeException("Check-in date must be before check-out date");
        }

        if(request.getCheckInDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Booking dates must be within the Airbnb's availability range");
        }

        List<Availability> availabilities = concurrencyControlStrategy.lockAndCheckAvailability(
                request.getAirbnbId(),
                request.getCheckInDate(),
                request.getCheckOutDate(),
                request.getUserId()
        );

        Long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        Double totalPrice = nights * airbnb.getPricePerNight();

        String idempotencyKey = UUID.randomUUID().toString();

        Booking booking = Booking.builder()
                .airbnbId(request.getAirbnbId())
                .userId(request.getUserId())
                .totalPrice(totalPrice)
                .idempotencyKey(idempotencyKey)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .build();

        log.info("Creating booking with idempotency key: {}", idempotencyKey);

        booking = bookingWriteRepository.save(booking);

        redisWriteRepository.writeBookingReadModel(booking);

        return booking;
    }

    @Override
    public Booking updateBooking(UpdateBookingRequest request) {
        return null;
    }
}
