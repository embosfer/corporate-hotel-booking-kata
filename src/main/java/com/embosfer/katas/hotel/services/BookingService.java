package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.model.*;

import java.time.LocalDate;
import java.util.Optional;

public class BookingService {

    private final HotelService hotelService;
    private final DatesValidator datesValidator;

    public BookingService(HotelService hotelService, DatesValidator datesValidator) {
        this.hotelService = hotelService;
        this.datesValidator = datesValidator;
    }

    public Booking book(EmployeeId employeeId, HotelId hotelId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {

        if (!datesValidator.validate(checkIn, checkOut)) {
            return BookingFailure.ofBadDates();
        }

        Optional<Hotel> hotel = hotelService.findHotelBy(hotelId);
        if (hotel.isEmpty()) {
            return BookingFailure.ofUnknownHotel();
        }

        return null;
    }
}
