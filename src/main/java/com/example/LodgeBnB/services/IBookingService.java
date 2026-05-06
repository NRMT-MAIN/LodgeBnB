package com.example.LodgeBnB.services;

import com.example.LodgeBnB.dtos.CreateBookingRequest;
import com.example.LodgeBnB.dtos.UpdateBookingRequest;
import com.example.LodgeBnB.models.Booking;

public interface IBookingService {
    Booking createBooking(CreateBookingRequest request);

    Booking updateBooking(UpdateBookingRequest request);

}
