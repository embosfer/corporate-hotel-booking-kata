package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.embosfer.katas.hotel.model.Booking.Reason.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock HotelService hotelService;
    @Mock DatesValidator datesValidator;
    BookingService bookingService;

    LocalDate checkIn = LocalDate.EPOCH;
    LocalDate checkOut = LocalDate.EPOCH;
    HotelId hotelId = HotelId.of("a-hotel");

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(hotelService, datesValidator);

        when(datesValidator.validate(any(), any())).thenReturn(true);
    }

    @Test
    void returnsBookingFailureIfHotelDoesNotExist() {

        when(hotelService.findHotelBy(hotelId)).thenReturn(Optional.empty());

        Booking bookingResult = bookingService.book(null, hotelId, null, checkIn, checkOut);

        assertThatIsBookingFailureOf(UNKNOWN_HOTEL, bookingResult);
    }

    @Test
    void returnsBookingFailureForIncorrectDates() {

        when(datesValidator.validate(checkIn, checkOut)).thenReturn(false);

        Booking bookingResult = bookingService.book(null, hotelId, null, checkIn, checkIn);

        assertThatIsBookingFailureOf(BAD_DATES, bookingResult);
    }

    @Test
    void returnsBookingFailureForUnavailableRoom() {

        Hotel hotelWithNoRooms = Hotel.of(hotelId);

        when(hotelService.findHotelBy(hotelId)).thenReturn(Optional.of(hotelWithNoRooms));

        Booking bookingResult = bookingService.book(null, hotelId, RoomType.SINGLE, checkIn, checkOut);

        assertThatIsBookingFailureOf(UNAVAILABLE_ROOM, bookingResult);
    }

    private void assertThatIsBookingFailureOf(Booking.Reason unknownHotel, Booking bookingResult) {
        assertThat(bookingResult.reason()).isEqualTo(unknownHotel);
    }

}