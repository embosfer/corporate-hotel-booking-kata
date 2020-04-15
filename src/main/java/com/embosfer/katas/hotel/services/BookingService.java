package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.model.Booking;
import com.embosfer.katas.hotel.model.EmployeeId;
import com.embosfer.katas.hotel.model.HotelId;
import com.embosfer.katas.hotel.model.RoomType;

import java.time.LocalDate;
import java.util.Optional;

public class BookingService {

    public Optional<Booking> book(EmployeeId employeeId, HotelId hotelId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        throw new RuntimeException("Not implemented");
    }
}
