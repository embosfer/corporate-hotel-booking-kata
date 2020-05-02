package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.caches.BookingRepository;
import com.embosfer.katas.hotel.model.Booking;
import com.embosfer.katas.hotel.model.Hotel;
import com.embosfer.katas.hotel.model.HotelId;
import com.embosfer.katas.hotel.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.embosfer.katas.hotel.model.Booking.Reason.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock HotelService hotelService;
    @Mock DatesValidator datesValidator;
    @Mock BookingRepository bookingRepository;
    BookingService bookingService;

    LocalDate checkIn = LocalDate.EPOCH;
    LocalDate checkOut = LocalDate.EPOCH.plus(1, DAYS);
    HotelId hotelId = HotelId.of("a-hotel");
    Hotel aHotel = Hotel.of(hotelId);;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingRepository, hotelService, datesValidator);

        when(datesValidator.validate(checkIn, checkOut)).thenReturn(true);
    }

    @Test
    void returnsBookingFailureForIncorrectDates() {
        when(datesValidator.validate(checkIn, checkOut)).thenReturn(false);

        Booking bookingResult = bookingService.book(null, hotelId, null, checkIn, checkOut);

        assertFalse(bookingResult.isOk());
        assertThat(bookingResult.reason()).isEqualTo(BAD_DATES);
    }

    @Test
    void returnsBookingFailureIfHotelDoesNotExist() {
        when(hotelService.findHotelBy(hotelId)).thenReturn(Optional.empty());

        Booking bookingResult = bookingService.book(null, hotelId, null, checkIn, checkOut);

        assertFalse(bookingResult.isOk());
        assertThat(bookingResult.reason()).isEqualTo(UNKNOWN_HOTEL);
    }

    @Test
    void returnsBookingFailureForUnavailableRoom() {
        when(hotelService.findHotelBy(hotelId)).thenReturn(Optional.of(aHotel));

        Booking bookingResult = bookingService.book(null, hotelId, RoomType.SINGLE, checkIn, checkOut);

        assertFalse(bookingResult.isOk());
        assertThat(bookingResult.reason()).isEqualTo(UNAVAILABLE_ROOM);
    }

    @Test
    void returnsBookingFailureForUnavailableRoomOnGivenDates() {
        var roomType = RoomType.SINGLE;
        aHotel.addRooms(roomType, 1);

        when(hotelService.findHotelBy(aHotel.id())).thenReturn(Optional.of(aHotel));
        when(bookingRepository.findExistingBookingsFor(aHotel.id(), roomType)).thenReturn(List.of(anExistingBookingFor(checkIn, checkOut)));

        Booking bookingResult = bookingService.book(null, aHotel.id(), roomType, checkIn, checkOut);

        assertFalse(bookingResult.isOk());
        assertThat(bookingResult.reason()).isEqualTo(UNAVAILABLE_DATES);
    }

    @Test
    void savesBookingUponBookingSuccess() {
        var roomType = RoomType.SINGLE;
        aHotel.addRooms(roomType, 1);

        when(hotelService.findHotelBy(aHotel.id())).thenReturn(Optional.of(aHotel));
        when(bookingRepository.findExistingBookingsFor(aHotel.id(), roomType)).thenReturn(emptyList());

        Booking bookingResult = bookingService.book(null, aHotel.id(), roomType, checkIn, checkOut);

        assertTrue(bookingResult.isOk());
        assertThat(bookingResult.reason()).isEqualTo(SUCCESS);
        verify(bookingRepository).save(bookingResult);
    }

    private Booking anExistingBookingFor(LocalDate checkIn, LocalDate checkOut) {
        return new Booking.Builder().checkIn(checkIn).checkOut(checkOut).build();
    }

}