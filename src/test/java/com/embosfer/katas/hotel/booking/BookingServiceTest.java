package com.embosfer.katas.hotel.booking;

import com.embosfer.katas.hotel.company.EmployeeId;
import com.embosfer.katas.hotel.hotel.Hotel;
import com.embosfer.katas.hotel.hotel.HotelId;
import com.embosfer.katas.hotel.hotel.RoomType;
import com.embosfer.katas.hotel.policy.BookingPolicyService;
import com.embosfer.katas.hotel.hotel.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.embosfer.katas.hotel.booking.Booking.Reason.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    HotelService hotelService;
    @Mock
    BookingPolicyService bookingPolicyService;
    @Mock
    DatesValidator datesValidator;
    @Mock BookingRepository bookingRepository;
    BookingService bookingService;

    LocalDate checkIn = LocalDate.EPOCH;
    LocalDate checkOut = LocalDate.EPOCH.plus(1, DAYS);
    HotelId hotelId = HotelId.of("a-hotel");
    Hotel aHotel = Hotel.of(hotelId);;
    EmployeeId employeeId;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingRepository, hotelService, bookingPolicyService, datesValidator);

        when(datesValidator.validate(checkIn, checkOut)).thenReturn(true);
    }

    @Test
    void returnsBookingFailureOfIncorrectDatesWhenValidatorSaysSo() {
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
    void returnsBookingFailureOfUnavailableRoomTypeWhenHotelDoesNotHaveIt() {
        when(hotelService.findHotelBy(hotelId)).thenReturn(Optional.of(aHotel));

        Booking bookingResult = bookingService.book(null, hotelId, RoomType.SINGLE, checkIn, checkOut);

        assertFalse(bookingResult.isOk());
        assertThat(bookingResult.reason()).isEqualTo(UNAVAILABLE_ROOM_TYPE);
    }

    @Test
    void returnsBookingFailureOfBookingDisallowedByPolicyIfBookingPolicyServiceSaysSo() {
        var roomType = RoomType.SINGLE;
        aHotel.setRoomsOf(roomType, 1);

        when(hotelService.findHotelBy(aHotel.id())).thenReturn(Optional.of(aHotel));
        when(bookingPolicyService.isBookingAllowed(employeeId, roomType)).thenReturn(false);

        Booking bookingResult = bookingService.book(null, hotelId, roomType, checkIn, checkOut);

        assertFalse(bookingResult.isOk());
        assertThat(bookingResult.reason()).isEqualTo(Booking.Reason.BOOKING_DISALLOWED_BY_POLICY);
    }

    @Test
    void returnsBookingFailureOfUnavailableRoomWhenNotAvailableOnGivenDates() {
        var roomType = RoomType.SINGLE;
        aHotel.setRoomsOf(roomType, 1);

        when(hotelService.findHotelBy(aHotel.id())).thenReturn(Optional.of(aHotel));
        when(bookingPolicyService.isBookingAllowed(employeeId, roomType)).thenReturn(true);
        when(bookingRepository.findExistingBookingsFor(aHotel.id(), roomType)).thenReturn(List.of(anExistingBookingFor(checkIn, checkOut)));

        Booking bookingResult = bookingService.book(null, aHotel.id(), roomType, checkIn, checkOut);

        assertFalse(bookingResult.isOk());
        assertThat(bookingResult.reason()).isEqualTo(NO_MORE_ROOMS_AVAILABLE_ON_GIVEN_DATES);
    }

    @Test
    void returnsBookingSuccessAndSavesBookingWhenNoBookingsHaveBeenMadeSoFar() {
        var roomType = RoomType.SINGLE;
        aHotel.setRoomsOf(roomType, 1);

        when(hotelService.findHotelBy(aHotel.id())).thenReturn(Optional.of(aHotel));
        when(bookingPolicyService.isBookingAllowed(employeeId, roomType)).thenReturn(true);
        when(bookingRepository.findExistingBookingsFor(aHotel.id(), roomType)).thenReturn(emptyList());

        Booking bookingResult = bookingService.book(null, aHotel.id(), roomType, checkIn, checkOut);

        assertSuccess(bookingResult);
    }

    @Test
    void returnsBookingSuccessAndSavesBookingWhenDatesAreNotOverlapping() {
        var roomType = RoomType.SINGLE;
        aHotel.setRoomsOf(roomType, 1);

        when(hotelService.findHotelBy(aHotel.id())).thenReturn(Optional.of(aHotel));
        when(bookingPolicyService.isBookingAllowed(employeeId, roomType)).thenReturn(true);
        when(bookingRepository.findExistingBookingsFor(aHotel.id(), roomType)).thenReturn(List.of(aNonOverlappingBooking()));

        Booking bookingResult = bookingService.book(null, aHotel.id(), roomType, checkIn, checkOut);

        assertSuccess(bookingResult);
    }

    @Test
    void returnsBookingSuccessAndSavesBookingWhenEnoughRoomsEvenIfDatesOverlap() {
        var roomType = RoomType.SINGLE;
        aHotel.setRoomsOf(roomType, 2);

        when(hotelService.findHotelBy(aHotel.id())).thenReturn(Optional.of(aHotel));
        when(bookingPolicyService.isBookingAllowed(employeeId, roomType)).thenReturn(true);
        when(bookingRepository.findExistingBookingsFor(aHotel.id(), roomType)).thenReturn(List.of(anExistingBookingFor(checkIn, checkOut)));

        Booking bookingResult = bookingService.book(null, aHotel.id(), roomType, checkIn, checkOut);

        assertSuccess(bookingResult);
    }

    private Booking anExistingBookingFor(LocalDate checkIn, LocalDate checkOut) {
        return new Booking.Builder().checkIn(checkIn).checkOut(checkOut).build();
    }

    private Booking aNonOverlappingBooking() {
        return anExistingBookingFor(checkOut.plus(1, DAYS), checkOut.plus(2, DAYS));
    }

    private void assertSuccess(Booking bookingResult) {
        assertTrue(bookingResult.isOk());
        assertThat(bookingResult.reason()).isEqualTo(SUCCESS);
        verify(bookingRepository).save(bookingResult);
    }

}