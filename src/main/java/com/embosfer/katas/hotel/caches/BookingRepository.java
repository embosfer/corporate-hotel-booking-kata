package com.embosfer.katas.hotel.caches;

import com.embosfer.katas.hotel.model.Booking;
import com.embosfer.katas.hotel.model.EmployeeId;
import com.embosfer.katas.hotel.model.HotelId;
import com.embosfer.katas.hotel.model.RoomType;

import java.util.*;

import static java.util.Collections.emptyList;

public class BookingRepository {

    private final Map<HotelAndRoomType, List<Booking>> bookings = new HashMap<>();

    private static class HotelAndRoomType {

        private final HotelId hotelId;
        private final RoomType roomType;

        private HotelAndRoomType(HotelId hotelId, RoomType roomType) {
            this.hotelId = hotelId;
            this.roomType = roomType;
        }

        public static HotelAndRoomType from(Booking booking) {
            return new HotelAndRoomType(booking.hotelId(), booking.roomType());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HotelAndRoomType that = (HotelAndRoomType) o;
            return hotelId.equals(that.hotelId) && roomType == that.roomType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(hotelId, roomType);
        }

    }

    public List<Booking> findExistingBookingsFor(HotelId hotelId, RoomType roomType) {
        return bookings.getOrDefault(new HotelAndRoomType(hotelId, roomType), emptyList());
    }

    public void save(Booking booking) {
        bookings.compute(HotelAndRoomType.from(booking), (hotelAndRoomType, bookings) -> {
            if (bookings == null) {
                bookings = new ArrayList<>();
            }
            bookings.add(booking);
            return bookings;
        });
    }

    public void deleteBookingsOf(EmployeeId employeeId) {
        // this could be terribly slow in a real hotel booking system, but it'll do for the kata
        bookings.forEach((h, bs) -> bs.removeIf(booking -> booking.employeeId().equals(employeeId)));
    }
}
