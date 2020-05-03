package com.embosfer.katas.hotel.caches;

import com.embosfer.katas.hotel.model.Hotel;
import com.embosfer.katas.hotel.model.HotelId;
import com.embosfer.katas.hotel.model.RoomType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HotelRepositoryTest {

    HotelRepository repository = new HotelRepository();

    @Test
    void returnsEmptyIfNoHotelFound() {
        assertThat(repository.findHotelBy(HotelId.of("unexistent"))).isEmpty();
    }

    @Test
    void canStoreAndFetchMultipleHotels() {
        Hotel hotel1 = Hotel.of(HotelId.of("h1"));
        Hotel hotel2 = Hotel.of(HotelId.of("h2"));

        repository.save(hotel1);
        repository.save(hotel2);

        Hotel actualHotel1 = repository.findHotelBy(HotelId.of("h1")).get();
        Hotel actualHotel2 = repository.findHotelBy(HotelId.of("h2")).get();

        assertThat(actualHotel1).isEqualTo(hotel1);
        assertThat(actualHotel2).isEqualTo(hotel2);

        hotel1.setRoomsOf(RoomType.SINGLE, 2);
        repository.save(hotel1);

        actualHotel1 = repository.findHotelBy(HotelId.of("h1")).get();
        assertThat(actualHotel1).isEqualTo(hotel1);
    }

}