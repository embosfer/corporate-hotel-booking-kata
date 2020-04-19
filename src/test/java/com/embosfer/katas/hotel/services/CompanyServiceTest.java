package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.caches.EmployeeRepository;
import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock EmployeeRepository employeeRepository;
    CompanyService companyService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyService(employeeRepository);
    }

    @Test
    void employeesAreStoredInTheEmployeeRepository() {
        CompanyId companyId = CompanyId.of("Acme");
        EmployeeId employeeId = EmployeeId.of(123);

        companyService.addEmployee(companyId, employeeId);

        verify(employeeRepository).addEmployee(companyId, employeeId);
    }

}