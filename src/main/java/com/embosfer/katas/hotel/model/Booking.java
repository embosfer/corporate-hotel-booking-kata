package com.embosfer.katas.hotel.model;

public class Booking {

    private final Reason reason;
    private final boolean isOk;

    private Booking(Reason reason, boolean isOk) {
        this.reason = reason;
        this.isOk = isOk;
    }

    public static Booking failureOfBadDates() {
        return new Booking(Reason.BAD_DATES, false);
    }

    public static Booking failureOfUnknownHotel() {
        return new Booking(Reason.UNKNOWN_HOTEL, false);
    }

    public static Booking failureOfUnavailableRoom() {
        return new Booking(Reason.UNAVAILABLE_ROOM, false);
    }

    public Reason reason() {
        return reason;
    }

    public boolean isOk() {
        return isOk;
    }

    public enum Reason {
        UNKNOWN_HOTEL, UNAVAILABLE_ROOM, BAD_DATES
    }
}
