package com.example.LodgeBnB.dtos;

import com.example.LodgeBnB.models.Booking;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBookingRequest {
    @NotNull(message = "Booking ID is required")
    private Long id;

    @NotNull(message = "Airbnb ID is required")
    private Long airbnbId;

    @NotNull(message = "Booking status is required")
    private Booking.BookingStatus bookingStatus;
}
