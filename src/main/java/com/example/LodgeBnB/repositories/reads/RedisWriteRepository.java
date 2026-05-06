package com.example.LodgeBnB.repositories.reads;

import com.example.LodgeBnB.models.Booking;
import com.example.LodgeBnB.models.readModels.BookingReadModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
public class RedisWriteRepository {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public void writeBookingReadModel(Booking booking) {
        BookingReadModel readModel = BookingReadModel.builder()
                .id(booking.getId())
                .airbnbId(booking.getAirbnbId())
                .userId(booking.getUserId())
                .totalPrice(booking.getTotalPrice())
                .bookingStatus(booking.getBookingStatus().toString())
                .idempotencyKey(booking.getIdempotencyKey())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .build();

        saveBookingReadModelToRedis(readModel);
    }

    private void saveBookingReadModelToRedis(BookingReadModel readModel) {
        String key = RedisReadRepository.BOOKING_KEY_PREFIX + readModel.getId();
        String value = objectMapper.writeValueAsString(readModel) ;
        redisTemplate.opsForValue().set(key, value);
    }
}
