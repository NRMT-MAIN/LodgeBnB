package com.example.LodgeBnB.services;

import com.example.LodgeBnB.models.Booking;

import java.util.Optional;

public interface IIdempotencyService {
    boolean isIdempotencyKeyUsed(String idempotencyKey);

    Optional<Booking> findBookingByIdempotencyKey(String idempotencyKey);
}
