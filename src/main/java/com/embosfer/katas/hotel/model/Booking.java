package com.embosfer.katas.hotel.model;

import java.time.LocalDate;

public class Booking {

    private final HotelId hotelId;
    private final RoomType roomType;
    private final Reason reason;
    private final boolean isOk;
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    private Booking(HotelId hotelId, RoomType roomType, Reason reason, LocalDate checkIn, LocalDate checkOut) {
        this.hotelId = hotelId;
        this.roomType = roomType;
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

    public RoomType roomType() {
        return roomType;
    }

    public HotelId hotelId() {
        return hotelId;
    }

    // assumes checkIn and checkOut are already sanitised
    public boolean overlaps(LocalDate checkIn, LocalDate checkOut) {
        if (checkOut.isBefore(this.checkIn) || checkOut.isEqual(this.checkIn)) {
            return false;
        }
        if (checkIn.isEqual(this.checkOut) || checkIn.isAfter(this.checkOut)) {
            return false;
        }
        return true;
    }

    public enum Reason {
        UNKNOWN_HOTEL, UNAVAILABLE_ROOM_TYPE, SUCCESS, NO_MORE_ROOMS_AVAILABLE_ON_GIVEN_DATES, BAD_DATES
    }

    public static class Builder {

        private LocalDate checkIn;
        private LocalDate checkOut;
        private Reason reason;
        private RoomType roomType;
        private HotelId hotelId;

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

        public Builder roomType(RoomType roomType) {
            this.roomType = roomType;
            return this;
        }

        public Builder hotel(HotelId hotelId) {
            this.hotelId = hotelId;
            return this;
        }

        public Booking build() {
            return new Booking(hotelId, roomType, reason, checkIn, checkOut);
        }
    }
}
