package com.embosfer.katas.hotel.model;

public class BookingFailure implements Booking {
    public Reason reason() {
        return Reason.UNKNOWN_HOTEL;
    }

    public enum Reason {
        UNKNOWN_HOTEL
    }
}
