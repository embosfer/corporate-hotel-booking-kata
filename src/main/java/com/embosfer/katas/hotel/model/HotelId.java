package com.embosfer.katas.hotel.model;

public class HotelId {
    private final String id;

    private HotelId(String id) {
        this.id = id;
    }

    public static HotelId of(String id) {
        return new HotelId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelId hotelId = (HotelId) o;
        return id.equals(hotelId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
