package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.caches.BookingPolicyRepository;
import com.embosfer.katas.hotel.caches.CompanyRepository;
import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;
import com.embosfer.katas.hotel.model.RoomType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BookingPolicyService {

    private final BookingPolicyRepository bookingPolicyRepository;
    private final CompanyRepository companyRepository;

    public BookingPolicyService(BookingPolicyRepository bookingPolicyRepository, CompanyRepository companyRepository) {
        this.bookingPolicyRepository = bookingPolicyRepository;
        this.companyRepository = companyRepository;
    }

    public void setCompanyPolicy(CompanyId companyId, Collection<RoomType> roomTypesAllowed) {
        bookingPolicyRepository.save(companyId, roomTypesAllowed);
    }

    public boolean isBookingAllowed(EmployeeId employeeId, RoomType roomType) {
        List<RoomType> roomsAllowed = companyRepository.findCompanyFor(employeeId)
                .stream()
                .map(bookingPolicyRepository::findRoomsAllowedFor)
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableList());

        if (roomsAllowed.isEmpty()) {
            return true;
        } else {
            return roomsAllowed.contains(roomType);
        }
    }
}
