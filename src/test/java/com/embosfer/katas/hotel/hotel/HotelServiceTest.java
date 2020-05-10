package com.embosfer.katas.hotel.hotel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock HotelRepository hotelRepository;
    HotelService hotelService;

    @BeforeEach
    void setUp() {
        hotelService = new HotelService(hotelRepository);
    }

    @Test
    void returnsEmptyIfHotelNotFound() {
        assertThat(hotelService.findHotelBy(HotelId.of("Unknown"))).isEmpty();
    }

    @Test
    void returnsHotelIfHotelFound() {
        HotelId hotelId = HotelId.of("Taj Mahal");
        Hotel hotel = Hotel.of(hotelId);
        when(hotelRepository.findHotelBy(hotelId)).thenReturn(Optional.of(hotel));

        assertThat(hotelService.findHotelBy(hotelId)).isEqualTo(Optional.of(hotel));
    }

    @Test
    void addsAnyNewHotelsIntoRepository() {
        HotelId hotelId = HotelId.of("Taj Mahal");
        RoomType roomType = RoomType.SINGLE;
        int rooms = 10;

        when(hotelRepository.findHotelBy(hotelId)).thenReturn(Optional.empty());

        hotelService.setRoomType(hotelId, roomType, rooms);

        verify(hotelRepository).save(hotel(hotelId, roomType, rooms));
    }

    private Hotel hotel(HotelId hotelId, RoomType roomType, int rooms) {
        Hotel hotel = Hotel.of(hotelId);
        hotel.setRoomsOf(roomType, rooms);
        return hotel;
    }
}