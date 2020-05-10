package com.embosfer.katas.hotel.hotel;

import java.util.Optional;

public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Optional<Hotel> findHotelBy(HotelId hotelId) {
        return hotelRepository.findHotelBy(hotelId);
    }

    public void setRoomType(HotelId hotelId, RoomType roomType, int quantity) {
        Hotel hotel = hotelRepository.findHotelBy(hotelId).orElse(Hotel.of(hotelId));
        hotel.setRoomsOf(roomType, quantity);
        hotelRepository.save(hotel);
    }
}
