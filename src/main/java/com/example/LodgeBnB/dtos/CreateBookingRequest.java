package com.example.LodgeBnB.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookingRequest {
    @NotNull(message = "Airbnb ID cannot be null")
    private Long airbnbId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Check-in date cannot be null")
    private String checkInDate;

    @NotNull(message = "Check-out date cannot be null")
    private String checkOutDate;
}
