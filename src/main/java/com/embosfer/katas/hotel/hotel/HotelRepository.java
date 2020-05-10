package com.embosfer.katas.hotel.hotel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HotelRepository {

    private final Map<HotelId, Hotel> hotels = new HashMap<>();

    public Optional<Hotel> findHotelBy(HotelId hotelId) {
        return Optional.ofNullable(hotels.get(hotelId));
    }

    public void save(Hotel hotel) {
        hotels.put(hotel.id(), hotel);
    }
}
