package com.example.LodgeBnB.services.concurrency;

import com.example.LodgeBnB.models.Availability;
import com.example.LodgeBnB.repositories.writes.AvailabilityWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisLockStrategy implements ConcurrencyControlStrategy {
    private static final String LOCK_KEY_PREFIX = "airbnb_lock:";
    private static final Duration LOCK_EXPIRATION = Duration.ofMinutes(2);

    private final RedisTemplate<String, String> redisTemplate;
    private final AvailabilityWriteRepository availabilityWriteRepository;

    private String generateLockKey(Long airbnbId, LocalDate checkInDate, LocalDate checkOutDate) {
        return LOCK_KEY_PREFIX + airbnbId + ":" + checkInDate + ":" + checkOutDate;
    }

    @Override
    public void releaseLock(Long airbnbId, LocalDate checkInDate, LocalDate checkOutDate) {
        String lockKey = generateLockKey(airbnbId, checkInDate, checkOutDate);
        redisTemplate.delete(lockKey);
    }


    @Override
    public List<Availability> lockAndCheckAvailability(Long airbnbId, LocalDate checkInDate, LocalDate checkOutDate, Long userId) {
        Long bookedSlots = availabilityWriteRepository.countByAirbnbIdAndDateBetweenAndBookingIdIsNotNull(airbnbId, checkInDate, checkOutDate);

        if(bookedSlots > 0) {
            throw new RuntimeException("Airbnb is not available for the selected dates");
        }

        String lockKey = generateLockKey(airbnbId, checkInDate, checkOutDate);
        boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, userId.toString(), LOCK_EXPIRATION);

        if(!locked) {
            throw new RuntimeException("Failed to acquire lock for Airbnb " + airbnbId);
        }

        try {
            return availabilityWriteRepository.findByAirbnbIdAndDateBetween(airbnbId, checkInDate, checkOutDate);
        } catch (Exception e) {
            releaseLock(airbnbId, checkInDate, checkOutDate);
            throw new RuntimeException("Error while checking availability for Airbnb " + airbnbId, e);
        }
    }
}
