package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.caches.HotelRepository;
import com.embosfer.katas.hotel.model.Hotel;
import com.embosfer.katas.hotel.model.HotelId;

import java.util.Optional;

public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Optional<Hotel> findHotelBy(HotelId hotelId) {
        return hotelRepository.getHotel(hotelId);
    }
}
