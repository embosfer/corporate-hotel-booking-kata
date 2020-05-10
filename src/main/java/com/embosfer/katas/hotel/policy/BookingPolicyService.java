package com.embosfer.katas.hotel.policy;

import com.embosfer.katas.hotel.company.CompanyRepository;
import com.embosfer.katas.hotel.company.CompanyId;
import com.embosfer.katas.hotel.company.EmployeeId;
import com.embosfer.katas.hotel.hotel.RoomType;

import java.util.Collection;

import static java.util.stream.Collectors.toUnmodifiableList;

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

    public void setEmployeePolicy(EmployeeId employeeId, Collection<RoomType> roomTypesAllowed) {
        bookingPolicyRepository.save(employeeId, roomTypesAllowed);
    }

    public boolean isBookingAllowed(EmployeeId employeeId, RoomType roomType) {
        var roomsAllowedForEmployee = bookingPolicyRepository.findRoomsAllowedFor(employeeId);
        if (policySet(roomsAllowedForEmployee)) {
            return roomsAllowedForEmployee.contains(roomType);
        }

        var roomsAllowedByCompany = companyRepository.findCompanyFor(employeeId)
                .stream()
                .map(bookingPolicyRepository::findRoomsAllowedFor)
                .flatMap(Collection::stream)
                .collect(toUnmodifiableList());

        if (policySet(roomsAllowedByCompany)) {
            return roomsAllowedByCompany.contains(roomType);
        } else {
            return true;
        }
    }

    private boolean policySet(Collection<RoomType> roomsAllowed) {
        return !roomsAllowed.isEmpty();
    }
}
