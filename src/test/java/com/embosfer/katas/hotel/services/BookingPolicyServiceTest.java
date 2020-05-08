package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.caches.BookingPolicyRepository;
import com.embosfer.katas.hotel.caches.CompanyRepository;
import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;
import com.embosfer.katas.hotel.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingPolicyServiceTest {

    static final CompanyId A_COMPANY = CompanyId.of("Acme");
    static final EmployeeId AN_EMPLOYEE_ID = EmployeeId.of(123);

    @Mock BookingPolicyRepository bookingPolicyRepository;
    @Mock CompanyRepository companyRepository;
    BookingPolicyService service;

    @BeforeEach
    void setUp() {
        service = new BookingPolicyService(bookingPolicyRepository, companyRepository);
    }

    @Test
    void bookingIsAllowedIfNoCompanyNorEmployeePolicyIsSet() {
        when(companyRepository.findCompanyFor(AN_EMPLOYEE_ID)).thenReturn(Optional.of(A_COMPANY));
        when(bookingPolicyRepository.findRoomsAllowedFor(A_COMPANY)).thenReturn(emptyList());

        assertTrue(service.isBookingAllowed(AN_EMPLOYEE_ID, RoomType.TWIN));
    }

    @Test
    void bookingIsAllowedIfCompanyPolicyIsSetForThatSameRoomType() {
        when(companyRepository.findCompanyFor(AN_EMPLOYEE_ID)).thenReturn(Optional.of(A_COMPANY));
        when(bookingPolicyRepository.findRoomsAllowedFor(A_COMPANY)).thenReturn(List.of(RoomType.TWIN));

        assertTrue(service.isBookingAllowed(AN_EMPLOYEE_ID, RoomType.TWIN));
    }

    @Test
    void bookingIsNotAllowedIfCompanyPolicyIsSetForADifferentRoomType() {
        when(companyRepository.findCompanyFor(AN_EMPLOYEE_ID)).thenReturn(Optional.of(A_COMPANY));
        when(bookingPolicyRepository.findRoomsAllowedFor(A_COMPANY)).thenReturn(List.of(RoomType.SINGLE));

        assertFalse(service.isBookingAllowed(AN_EMPLOYEE_ID, RoomType.DOUBLE));
    }

    @Test
    void companyPolicyGetsSavedInRepository() {
        service.setCompanyPolicy(A_COMPANY, List.of(RoomType.TWIN));
        verify(bookingPolicyRepository).save(A_COMPANY, List.of(RoomType.TWIN));
    }

}