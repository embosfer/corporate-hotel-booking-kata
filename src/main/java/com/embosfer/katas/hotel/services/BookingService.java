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

        Booking.Builder booking = new Booking.Builder().checkIn(checkIn).checkOut(checkOut);

        if (!datesValidator.validate(checkIn, checkOut)) {
            return booking.reason(Booking.Reason.BAD_DATES).build();
        }

        Optional<Hotel> hotel = hotelService.findHotelBy(hotelId);
        if (hotel.isEmpty()) {
            return booking.reason(Booking.Reason.UNKNOWN_HOTEL).build();
        }

        if (hotel.get().availableRoomsOf(roomType) == 0) {
            return booking.reason(Booking.Reason.UNAVAILABLE_ROOM).build();
        }

        return booking.reason(Booking.Reason.SUCCESS).build();
    }
}
