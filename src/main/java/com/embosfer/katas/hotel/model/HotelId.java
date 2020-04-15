package com.embosfer.katas.hotel.model;

public class HotelId {
    private final String id;

    private HotelId(String id) {
        this.id = id;
    }

    public static HotelId of(String id) {
        return new HotelId(id);
    }
}
