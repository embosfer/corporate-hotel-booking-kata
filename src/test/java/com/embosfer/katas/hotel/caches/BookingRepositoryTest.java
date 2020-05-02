package com.embosfer.katas.hotel.caches;

import com.embosfer.katas.hotel.model.Booking;
import com.embosfer.katas.hotel.model.HotelId;
import com.embosfer.katas.hotel.model.RoomType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookingRepositoryTest {

    BookingRepository repository = new BookingRepository();

    @Test
    void canStoreAndFetchBookingsForMultipleHotels() {
        assertThat(repository.findExistingBookingsFor(HotelId.of("unexistent"), RoomType.SINGLE)).isEmpty();

        var singleRoomBooking = aBookingFor(HotelId.of("id-1"), RoomType.SINGLE);
        repository.save(singleRoomBooking);
        assertThat(repository.findExistingBookingsFor(HotelId.of("id-1"), RoomType.SINGLE)).containsExactly(singleRoomBooking);

        var singleRoomBooking2 = aBookingFor(HotelId.of("id-1"), RoomType.SINGLE);
        repository.save(singleRoomBooking2);
        assertThat(repository.findExistingBookingsFor(HotelId.of("id-1"), RoomType.SINGLE)).containsExactly(singleRoomBooking, singleRoomBooking2);

        var doubleRoomBooking = aBookingFor(HotelId.of("id-1"), RoomType.DOUBLE);
        repository.save(doubleRoomBooking);
        assertThat(repository.findExistingBookingsFor(HotelId.of("id-1"), RoomType.DOUBLE)).containsExactly(doubleRoomBooking);

        // a different hotel now
        var twinRoomBooking = aBookingFor(HotelId.of("id-2"), RoomType.TWIN);
        repository.save(twinRoomBooking);
        assertThat(repository.findExistingBookingsFor(HotelId.of("id-2"), RoomType.TWIN)).containsExactly(twinRoomBooking);
    }

    private Booking aBookingFor(HotelId hotelId, RoomType roomType) {
        return new Booking.Builder().hotel(hotelId).roomType(roomType).build();
    }

}