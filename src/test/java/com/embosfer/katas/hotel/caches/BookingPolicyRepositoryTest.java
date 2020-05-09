package com.embosfer.katas.hotel.caches;

import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;
import com.embosfer.katas.hotel.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookingPolicyRepositoryTest {

    static final CompanyId A_COMPANY_ID = CompanyId.of("Acme");
    static final EmployeeId AN_EMPLOYEE_ID = EmployeeId.of(123);
    private BookingPolicyRepository repository;

    @BeforeEach
    void setUp() {
        repository = new BookingPolicyRepository();
    }

    @Test
    void returnsEmptyListIfNoRoomsHaveBeenSavedForACompany() {
        assertThat(repository.findRoomsAllowedFor(A_COMPANY_ID)).isEmpty();
    }

    @Test
    void returnsEmptyListIfNoRoomsHaveBeenSavedForAnEmployee() {
        assertThat(repository.findRoomsAllowedFor(AN_EMPLOYEE_ID)).isEmpty();
    }

    @Test
    void canSaveABookingPolicyAndQueryForItLater() {
        repository.save(A_COMPANY_ID, List.of(RoomType.SINGLE));

        assertThat(repository.findRoomsAllowedFor(A_COMPANY_ID)).containsExactly(RoomType.SINGLE);
    }

    @Test
    void canSaveAnEmployeeBookingPolicyAndQueryForItLater() {
        repository.save(AN_EMPLOYEE_ID, List.of(RoomType.SINGLE, RoomType.DOUBLE));

        assertThat(repository.findRoomsAllowedFor(AN_EMPLOYEE_ID)).containsExactly(RoomType.SINGLE, RoomType.DOUBLE);
    }

}