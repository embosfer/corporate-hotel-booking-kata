package com.embosfer.katas.hotel.model;

import java.util.*;

public class Hotel {

    private final HotelId id;
    private final Map<RoomType, Integer> rooms = new HashMap<>();

    private Hotel(HotelId id) {
        this.id = id;
    }

    public static Hotel of(HotelId hotelId) {
        return new Hotel(hotelId);
    }

    public HotelId id() {
        return id;
    }

    public void addRooms(RoomType roomType, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Number of rooms cannot be <= 0");
        }
        this.rooms.put(roomType, quantity);
    }

    public int numberOfRoomsOf(RoomType roomType) {
        return rooms.getOrDefault(roomType, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel other = (Hotel) o;
        return id.equals(other.id) && rooms.equals(other.rooms);
    }
}
