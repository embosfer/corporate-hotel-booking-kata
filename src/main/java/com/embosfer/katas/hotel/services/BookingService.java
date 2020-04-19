package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.model.*;

import java.time.LocalDate;
import java.util.Optional;

public class BookingService {

    private final HotelService hotelService;

    public BookingService(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    public Booking book(EmployeeId employeeId, HotelId hotelId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        Optional<Hotel> hotel = hotelService.findHotelBy(hotelId);
        if (hotel.isEmpty()) {
            return new BookingFailure();
        }

        return null;
    }
}
