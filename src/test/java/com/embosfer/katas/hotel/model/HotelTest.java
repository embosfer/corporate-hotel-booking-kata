package com.embosfer.katas.hotel.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HotelTest {

    @Test
    void canUpdateNumberOfRoomsForARoomType() {
        Hotel hotel = Hotel.of(HotelId.of("id"));
        assertThat(hotel.availableRoomsOf(RoomType.SINGLE)).isEqualTo(0);
        assertThat(hotel.availableRoomsOf(RoomType.DOUBLE)).isEqualTo(0);

        hotel.addRooms(RoomType.SINGLE, 5);
        hotel.addRooms(RoomType.DOUBLE, 42);
        assertThat(hotel.availableRoomsOf(RoomType.SINGLE)).isEqualTo(5);
        assertThat(hotel.availableRoomsOf(RoomType.DOUBLE)).isEqualTo(42);
    }

}