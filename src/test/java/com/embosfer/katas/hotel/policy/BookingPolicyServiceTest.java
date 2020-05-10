package com.embosfer.katas.hotel.policy;

import com.embosfer.katas.hotel.company.CompanyRepository;
import com.embosfer.katas.hotel.company.CompanyId;
import com.embosfer.katas.hotel.company.EmployeeId;
import com.embosfer.katas.hotel.hotel.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingPolicyServiceTest {

    static final CompanyId A_COMPANY_ID = CompanyId.of("Acme");
    static final EmployeeId AN_EMPLOYEE_ID = EmployeeId.of(123);

    @Mock BookingPolicyRepository bookingPolicyRepository;
    @Mock CompanyRepository companyRepository;
    BookingPolicyService service;

    @BeforeEach
    void setUp() {
        service = new BookingPolicyService(bookingPolicyRepository, companyRepository);
    }

    @Test
    void whenNoEmployeeNorCompanyPolicyAreSetThenBookingIsAllowed() {
        noEmployeePolicySet();
        noCompanyPolicySet();
        when(companyRepository.findCompanyFor(AN_EMPLOYEE_ID)).thenReturn(Optional.of(A_COMPANY_ID));

        assertTrue(service.isBookingAllowed(AN_EMPLOYEE_ID, RoomType.TWIN));
    }

    @Test
    void whenNoEmployeePolicyIsSetAndCompanyPolicyIsSetForARoomTypeThenBookingIsAllowedForThatSameRoomType() {
        noEmployeePolicySet();
        companyPolicyAllows(RoomType.TWIN);
        when(companyRepository.findCompanyFor(AN_EMPLOYEE_ID)).thenReturn(Optional.of(A_COMPANY_ID));

        assertTrue(service.isBookingAllowed(AN_EMPLOYEE_ID, RoomType.TWIN));
    }

    @Test
    void whenNoEmployeePolicyIsSetAndCompanyPolicyIsSetForARoomTypeThenBookingIsNotAllowedForADifferentRoomType() {
        noEmployeePolicySet();
        when(companyRepository.findCompanyFor(AN_EMPLOYEE_ID)).thenReturn(Optional.of(A_COMPANY_ID));
        companyPolicyAllows(RoomType.SINGLE);

        assertFalse(service.isBookingAllowed(AN_EMPLOYEE_ID, RoomType.DOUBLE));
    }

    @Test
    void whenEmployeePolicyIsSetThenItTakesPrecedenceOverCompanyPolicy() {
        employeePolicyAllows(RoomType.TWIN);

        assertTrue(service.isBookingAllowed(AN_EMPLOYEE_ID, RoomType.TWIN));
        verifyNoInteractions(companyRepository);
        verify(bookingPolicyRepository, times(0)).findRoomsAllowedFor(A_COMPANY_ID);
    }

    @Test
    void whenEmployeePolicyIsSetForARoomTypeThenBookingIsAllowedForThatSameRoomType() {
        employeePolicyAllows(RoomType.TWIN);

        assertTrue(service.isBookingAllowed(AN_EMPLOYEE_ID, RoomType.TWIN));
    }

    @Test
    void whenEmployeePolicyIsSetForARoomTypeThenBookingIsNotAllowedForADifferentRoomType() {
        employeePolicyAllows(RoomType.TWIN);

        assertFalse(service.isBookingAllowed(AN_EMPLOYEE_ID, RoomType.SINGLE));
    }

    @Test
    void companyPolicyGetsSavedInRepository() {
        service.setCompanyPolicy(A_COMPANY_ID, List.of(RoomType.TWIN));
        verify(bookingPolicyRepository).save(A_COMPANY_ID, List.of(RoomType.TWIN));
    }

    @Test
    void employeePolicyGetsSavedInRepository() {
        service.setEmployeePolicy(AN_EMPLOYEE_ID, List.of(RoomType.DOUBLE));
        verify(bookingPolicyRepository).save(AN_EMPLOYEE_ID, List.of(RoomType.DOUBLE));
    }

    private void noCompanyPolicySet() {
        when(bookingPolicyRepository.findRoomsAllowedFor(A_COMPANY_ID)).thenReturn(emptyList());
    }

    private void noEmployeePolicySet() {
        when(bookingPolicyRepository.findRoomsAllowedFor(AN_EMPLOYEE_ID)).thenReturn(emptyList());
    }

    private void companyPolicyAllows(RoomType roomType) {
        when(bookingPolicyRepository.findRoomsAllowedFor(A_COMPANY_ID)).thenReturn(List.of(roomType));
    }

    private void employeePolicyAllows(RoomType roomType) {
        when(bookingPolicyRepository.findRoomsAllowedFor(AN_EMPLOYEE_ID)).thenReturn(List.of(roomType));
    }

}