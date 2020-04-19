package com.embosfer.katas.hotel.model;

public class BookingFailure implements Booking {
    private final Reason reason;

    private BookingFailure(Reason reason) {
        this.reason = reason;
    }

    public static Booking ofBadDates() {
        return new BookingFailure(Reason.BAD_DATES);
    }

    public static Booking ofUnknownHotel() {
        return new BookingFailure(Reason.UNKNOWN_HOTEL);
    }

    public Reason reason() {
        return reason;
    }

    public enum Reason {
        UNKNOWN_HOTEL, BAD_DATES
    }
}
