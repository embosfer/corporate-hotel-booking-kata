package com.embosfer.katas.hotel.caches;

import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookingPolicyRepositoryTest {

    static final CompanyId A_COMPANY_ID = CompanyId.of("Acme");
    private BookingPolicyRepository repository;

    @BeforeEach
    void setUp() {
        repository = new BookingPolicyRepository();
    }

    @Test
    void emptyListMeansNoPolicyForACompany() {
        assertThat(repository.findRoomsAllowedFor(A_COMPANY_ID)).isEmpty();
    }

    @Test
    void canSaveABookingPolicyAndQueryForItLater() {
        repository.save(A_COMPANY_ID, List.of(RoomType.SINGLE));

        assertThat(repository.findRoomsAllowedFor(A_COMPANY_ID)).containsExactly(RoomType.SINGLE);
    }


}