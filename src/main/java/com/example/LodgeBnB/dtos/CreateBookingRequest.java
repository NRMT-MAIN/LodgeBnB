package com.example.LodgeBnB.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBookingRequest {
    @NotNull(message = "Airbnb ID cannot be null")
    private Long airbnbId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Check-in date cannot be null")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date cannot be null")
    private LocalDate checkOutDate;
}
