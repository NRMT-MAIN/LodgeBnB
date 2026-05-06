package com.example.LodgeBnB.dtos;

import com.example.LodgeBnB.models.Booking;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBookingRequest {
    @NotNull(message = "Booking ID is required")
    private Long id;

    @NotNull(message = "Idempotency key is required")
    private String idempotencyKey;

    @NotNull(message = "Booking status is required")
    private Booking.BookingStatus bookingStatus;
}
