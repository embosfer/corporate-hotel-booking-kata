package com.embosfer.katas.hotel.company;

import com.embosfer.katas.hotel.policy.BookingPolicyRepository;
import com.embosfer.katas.hotel.booking.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    static final CompanyId A_COMPANY = CompanyId.of("Acme");
    static final EmployeeId AN_EMPLOYEE = EmployeeId.of(123);

    @Mock CompanyRepository companyRepository;
    @Mock BookingPolicyRepository bookingPolicyRepository;
    @Mock BookingRepository bookingRepository;
    CompanyService companyService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyService(companyRepository, bookingPolicyRepository, bookingRepository);
    }

    @Test
    void employeesAreStoredInTheEmployeeRepository() {
        companyService.addEmployee(A_COMPANY, AN_EMPLOYEE);

        verify(companyRepository).addEmployee(A_COMPANY, AN_EMPLOYEE);
    }

    @Test
    void employeesCannotBeDuplicated() {
        when(companyRepository.findCompanyFor(AN_EMPLOYEE)).thenReturn(Optional.of(A_COMPANY));

        assertThatThrownBy(() -> companyService.addEmployee(A_COMPANY, AN_EMPLOYEE))
                .isInstanceOf(EmployeeAlreadyExistsException.class);
    }

    @Test
    void whenEmployeeGetsDeletedThenEmployeeGetsDeletedFromRepoAndPoliciesAndBookingsForThatEmployeeGetDeletedToo() {
        companyService.deleteEmployee(AN_EMPLOYEE);

        verify(companyRepository).deleteEmployee(AN_EMPLOYEE);
        verify(bookingPolicyRepository).deletePolicyOf(AN_EMPLOYEE);
        verify(bookingRepository).deleteBookingsOf(AN_EMPLOYEE);
    }
}