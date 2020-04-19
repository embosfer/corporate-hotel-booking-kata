package com.embosfer.katas.hotel.model;

public class Hotel {

    private final HotelId hotelId;

    private Hotel(HotelId hotelId) {
        this.hotelId = hotelId;
    }

    public static Hotel of(HotelId hotelId) {
        return new Hotel(hotelId);
    }
}
