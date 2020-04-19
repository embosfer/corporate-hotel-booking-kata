package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.model.Booking;
import com.embosfer.katas.hotel.model.BookingFailure;
import com.embosfer.katas.hotel.model.EmployeeId;
import com.embosfer.katas.hotel.model.HotelId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.embosfer.katas.hotel.model.BookingFailure.Reason.UNKNOWN_HOTEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock HotelService hotelService;
    BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(hotelService);
    }

    @Test
    void returnsBookingFailureIfHotelDoesNotExist() {

        HotelId hotelId = HotelId.of("Unknown");
        when(hotelService.findHotelBy(hotelId)).thenReturn(Optional.empty());

        Booking bookingResult = bookingService.book(EmployeeId.of(123), hotelId, null, null, null);

        assertThat(bookingResult).isInstanceOf(BookingFailure.class);
        BookingFailure failure = (BookingFailure) bookingResult;
        assertThat(failure.reason()).isEqualTo(UNKNOWN_HOTEL);
    }

}