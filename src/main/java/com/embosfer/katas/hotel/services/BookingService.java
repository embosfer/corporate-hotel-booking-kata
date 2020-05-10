package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.caches.BookingRepository;
import com.embosfer.katas.hotel.model.*;

import java.time.LocalDate;
import java.util.Optional;

import static com.embosfer.katas.hotel.model.Booking.Reason.*;
import static java.util.stream.Collectors.toUnmodifiableList;

public class BookingService {

    private final BookingRepository bookingRepository;
    private final HotelService hotelService;
    private final BookingPolicyService bookingPolicyService;
    private final DatesValidator datesValidator;

    public BookingService(BookingRepository bookingRepository, HotelService hotelService, BookingPolicyService bookingPolicyService, DatesValidator datesValidator) {
        this.bookingRepository = bookingRepository;
        this.hotelService = hotelService;
        this.bookingPolicyService = bookingPolicyService;
        this.datesValidator = datesValidator;
    }

    public Booking book(EmployeeId employeeId, HotelId hotelId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {

        Booking.Builder booking = new Booking.Builder().employee(employeeId).hotel(hotelId).roomType(roomType).checkIn(checkIn).checkOut(checkOut);

        if (!datesValidator.validate(checkIn, checkOut)) {
            return booking.reason(BAD_DATES).build();
        }

        Optional<Hotel> hotel = hotelService.findHotelBy(hotelId);
        if (hotel.isEmpty()) {
            return booking.reason(UNKNOWN_HOTEL).build();
        }

        var roomsOfType = hotel.get().numberOfRoomsOf(roomType);
        if (roomsOfType == 0) {
            return booking.reason(UNAVAILABLE_ROOM_TYPE).build();
        }

        if (!bookingPolicyService.isBookingAllowed(employeeId, roomType)) {
            return booking.reason(BOOKING_DISALLOWED_BY_POLICY).build();
        }

        var overlappingBookings = bookingRepository.findExistingBookingsFor(hotelId, roomType)
                .stream()
                .filter(b -> b.overlaps(checkIn, checkOut))
                .collect(toUnmodifiableList());
        if (overlappingBookings.size() == roomsOfType) {
            return booking.reason(NO_MORE_ROOMS_AVAILABLE_ON_GIVEN_DATES).build();
        }

        var bookingSuccess = booking.reason(SUCCESS).build();
        bookingRepository.save(bookingSuccess);

        return bookingSuccess;
    }
}
