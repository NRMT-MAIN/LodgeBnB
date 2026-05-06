package com.example.LodgeBnB.services;

import com.example.LodgeBnB.models.Booking;
import com.example.LodgeBnB.models.readModels.BookingReadModel;
import com.example.LodgeBnB.repositories.reads.RedisReadRepository;
import com.example.LodgeBnB.repositories.writes.BookingWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService implements IIdempotencyService{
    private final RedisReadRepository redisReadRepository;
    private final BookingWriteRepository bookingWriteRepository;

    @Override
    public boolean isIdempotencyKeyUsed(String idempotencyKey) {
        return false;
    }

    @Override
    public Optional<Booking> findBookingByIdempotencyKey(String idempotencyKey) {
        BookingReadModel bookingReadModel = redisReadRepository.findBookingByIdempotencyKey(idempotencyKey);

        if(bookingReadModel != null) {
            return Optional.ofNullable(Booking.builder()
                    .id(bookingReadModel.getId())
                    .airbnbId(bookingReadModel.getAirbnbId())
                    .userId(bookingReadModel.getUserId())
                    .totalPrice(bookingReadModel.getTotalPrice())
                    .bookingStatus(Booking.BookingStatus.valueOf(bookingReadModel.getBookingStatus()))
                    .idempotencyKey(bookingReadModel.getIdempotencyKey())
                    .checkInDate(bookingReadModel.getCheckInDate())
                    .checkOutDate(bookingReadModel.getCheckOutDate())
                    .build());
        }

        return bookingWriteRepository.findByIdempotencyKey(idempotencyKey);
    }
}
