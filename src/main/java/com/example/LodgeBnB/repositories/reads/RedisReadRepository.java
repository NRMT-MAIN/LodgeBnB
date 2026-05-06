package com.example.LodgeBnB.repositories.reads;

import com.example.LodgeBnB.models.readModels.AirbnbReadModel;
import com.example.LodgeBnB.models.readModels.AvailabilityReadModel;
import com.example.LodgeBnB.models.readModels.BookingReadModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisReadRepository {
    public static final String AIRBNB_KEY_PREFIX = "airbnb:";
    public static final String BOOKING_KEY_PREFIX = "booking:";
    public static final String AVAILABILITY_KEY_PREFIX = "availability:";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public AirbnbReadModel findAirbnbById(Long id) {
        String key = AIRBNB_KEY_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            try {
                return objectMapper.readValue(value, AirbnbReadModel.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize AirbnbReadModel from Redis", e);
            }
        }
        return null;
    }

    public BookingReadModel findBookingById(Long id) {
        String key = BOOKING_KEY_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            try {
                return objectMapper.readValue(value, BookingReadModel.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize BookingReadModel from Redis", e);
            }
        }
        return null;
    }

    public AvailabilityReadModel findAvailabilityById(Long id) {
        String key = AVAILABILITY_KEY_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            try {
                return objectMapper.readValue(value, AvailabilityReadModel.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize AvailabilityReadModel from Redis", e);
            }
        }
        return null;
    }

    public List<AirbnbReadModel> findAllAirbnbs() {
        Set<String> keys = redisTemplate.keys(AIRBNB_KEY_PREFIX + "*");

        if(keys == null || keys.isEmpty()) {
            return List.of();
        }

        return keys.stream()
                .map(key -> {
                    String value = redisTemplate.opsForValue().get(key);
                    if (value != null) {
                        try {
                            return objectMapper.readValue(value, AirbnbReadModel.class);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to deserialize AirbnbReadModel from Redis", e);
                        }
                    }
                    return null;
                })
                .filter(airbnb -> airbnb != null)
                .toList();
    }

    public BookingReadModel findBookingByIdempotencyKey(String idempotencyKey) {
        Set<String> keys = redisTemplate.keys(BOOKING_KEY_PREFIX + "*");

        if(keys == null || keys.isEmpty()) {
            return null;
        }

        return keys.stream()
                .map(key -> {
                    String value = redisTemplate.opsForValue().get(key);
                    if (value != null) {
                        try {
                            BookingReadModel booking = objectMapper.readValue(value, BookingReadModel.class);
                            if (booking.getIdempotencyKey().equals(idempotencyKey)) {
                                return booking;
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to deserialize BookingReadModel from Redis", e);
                        }
                    }
                    return null;
                })
                .filter(booking -> booking != null)
                .findFirst()
                .orElse(null);
    }
}
