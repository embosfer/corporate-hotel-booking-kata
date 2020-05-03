package com.embosfer.katas.hotel.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class HotelTest {

    @Test
    void canUpdateNumberOfRoomsForARoomType() {
        Hotel hotel = Hotel.of(HotelId.of("id"));
        assertThat(hotel.numberOfRoomsOf(RoomType.SINGLE)).isEqualTo(0);
        assertThat(hotel.numberOfRoomsOf(RoomType.DOUBLE)).isEqualTo(0);

        hotel.addRooms(RoomType.SINGLE, 5);
        hotel.addRooms(RoomType.DOUBLE, 42);
        assertThat(hotel.numberOfRoomsOf(RoomType.SINGLE)).isEqualTo(5);
        assertThat(hotel.numberOfRoomsOf(RoomType.DOUBLE)).isEqualTo(42);
    }

    @Test
    void throwsExceptionWhenNumberOfRoomsIsNegativeOrZero() {
        Hotel hotel = Hotel.of(HotelId.of("id"));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> hotel.addRooms(RoomType.SINGLE, 0));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> hotel.addRooms(RoomType.SINGLE, -1));
    }

}