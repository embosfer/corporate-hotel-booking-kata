package com.embosfer.katas.hotel.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingTest {

    @Test
    void canDetectOverlappingBookings() {
        // exact match
        var booking1 = aBookingFor(LocalDate.EPOCH, LocalDate.EPOCH.plus(2, DAYS));
        var booking2 = aBookingFor(LocalDate.EPOCH, LocalDate.EPOCH.plus(2, DAYS));
        assertOverlap(booking1, booking2);

        // overlap on the checkout side (before checkout)
        booking1 = aBookingFor(LocalDate.EPOCH, LocalDate.EPOCH.plus(2, DAYS));
        booking2 = aBookingFor(booking1.checkIn().minus(1, DAYS), booking1.checkIn().plus(1, DAYS));
        assertOverlap(booking1, booking2);

        // overlap on the checkout side (same as checkout)
        booking1 = aBookingFor(LocalDate.EPOCH, LocalDate.EPOCH.plus(2, DAYS));
        booking2 = aBookingFor(booking1.checkIn().minus(1, DAYS), booking1.checkOut());
        assertOverlap(booking1, booking2);

        // overlap on the checkout side (after checkout)
        booking1 = aBookingFor(LocalDate.EPOCH, LocalDate.EPOCH.plus(1, DAYS));
        booking2 = aBookingFor(booking1.checkIn().minus(1, DAYS), booking1.checkOut().plus(1, DAYS));
        assertOverlap(booking1, booking2);

        // overlap on the checkin side (same as checkin)
        booking1 = aBookingFor(LocalDate.EPOCH, LocalDate.EPOCH.plus(2, DAYS));
        booking2 = aBookingFor(booking1.checkIn(), booking1.checkOut().plus(1, DAYS));
        assertOverlap(booking1, booking2);

        // overlap on the checkin side (after checkin)
        booking1 = aBookingFor(LocalDate.EPOCH, LocalDate.EPOCH.plus(2, DAYS));
        booking2 = aBookingFor(booking1.checkIn().plus(1, DAYS), booking1.checkOut().plus(1, DAYS));
        assertOverlap(booking1, booking2);

        // no match (before)
        booking1 = aBookingFor(LocalDate.EPOCH, LocalDate.EPOCH.plus(1, DAYS));
        booking2 = aBookingFor(booking1.checkIn().minus(1, DAYS), booking1.checkIn());
        assertNotOverlap(booking1, booking2);

        // no match (after)
        booking1 = aBookingFor(LocalDate.EPOCH, LocalDate.EPOCH.plus(1, DAYS));
        booking2 = aBookingFor(booking1.checkOut(), booking1.checkOut().plus(1, DAYS));
        assertNotOverlap(booking1, booking2);
    }

    private void assertNotOverlap(Booking booking1, Booking booking2) {
        assertFalse(booking1.overlaps(booking2.checkIn(), booking2.checkOut()));
    }

    private void assertOverlap(Booking booking1, Booking booking2) {
        assertTrue(booking1.overlaps(booking2.checkIn(), booking2.checkOut()));
    }

    private Booking aBookingFor(LocalDate checkIn, LocalDate checkOut) {
        return new Booking.Builder().checkIn(checkIn).checkOut(checkOut).build();
    }

}