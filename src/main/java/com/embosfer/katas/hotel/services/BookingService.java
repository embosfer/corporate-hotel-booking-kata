package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.caches.BookingRepository;
import com.embosfer.katas.hotel.model.*;

import java.time.LocalDate;
import java.util.Optional;

import static com.embosfer.katas.hotel.model.Booking.Reason.*;

public class BookingService {

    private final BookingRepository bookingRepository;
    private final HotelService hotelService;
    private final DatesValidator datesValidator;

    public BookingService(BookingRepository bookingRepository, HotelService hotelService, DatesValidator datesValidator) {
        this.bookingRepository = bookingRepository;
        this.hotelService = hotelService;
        this.datesValidator = datesValidator;
    }

    public Booking book(EmployeeId employeeId, HotelId hotelId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {

        Booking.Builder booking = new Booking.Builder().hotel(hotelId).roomType(roomType).checkIn(checkIn).checkOut(checkOut);

        if (!datesValidator.validate(checkIn, checkOut)) {
            return booking.reason(BAD_DATES).build();
        }

        Optional<Hotel> hotel = hotelService.findHotelBy(hotelId);
        if (hotel.isEmpty()) {
            return booking.reason(UNKNOWN_HOTEL).build();
        }

        if (hotel.get().availableRoomsOf(roomType) == 0) {
            return booking.reason(UNAVAILABLE_ROOM).build();
        }

        var bookingOverlaps = bookingRepository.findExistingBookingsFor(hotelId, roomType).stream()
                .anyMatch(b -> b.overlaps(checkIn, checkOut));
        if (bookingOverlaps) {
            return booking.reason(UNAVAILABLE_DATES).build();
        }

        var bookingSucess = booking.reason(SUCCESS).build();
        bookingRepository.save(bookingSucess);

        return bookingSucess;
    }
}
