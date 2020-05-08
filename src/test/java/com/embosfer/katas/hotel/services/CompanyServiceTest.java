package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.caches.CompanyRepository;
import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    static final CompanyId A_COMPANY = CompanyId.of("Acme");
    static final EmployeeId AN_EMPLOYEE = EmployeeId.of(123);

    @Mock
    CompanyRepository companyRepository;
    CompanyService companyService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyService(companyRepository);
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
}