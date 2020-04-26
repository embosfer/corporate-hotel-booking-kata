package com.embosfer.katas.hotel.model;

import java.time.LocalDate;

public class Booking {

    private final Reason reason;
    private final boolean isOk;
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    private Booking(Reason reason, LocalDate checkIn, LocalDate checkOut) {
        this.reason = reason;
        this.isOk = reason == Reason.SUCCESS;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Reason reason() {
        return reason;
    }

    public boolean isOk() {
        return isOk;
    }

    public LocalDate checkIn() {
        return checkIn;
    }

    public LocalDate checkOut() {
        return checkOut;
    }

    public enum Reason {
        UNKNOWN_HOTEL, UNAVAILABLE_ROOM, SUCCESS, BAD_DATES
    }

    public static class Builder {

        private LocalDate checkIn;
        private LocalDate checkOut;
        private Reason reason;

        public Booking.Builder checkIn(LocalDate checkIn) {
            this.checkIn = checkIn;
            return this;
        }

        public Builder checkOut(LocalDate checkOut) {
            this.checkOut = checkOut;
            return this;
        }

        public Builder reason(Reason reason) {
            this.reason = reason;
            return this;
        }

        public Booking build() {
            return new Booking(reason, checkIn, checkOut);
        }
    }
}
