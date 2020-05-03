package com.embosfer.katas.hotel.caches;

import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class EmployeeRepositoryTest {

    EmployeeRepository repository = new EmployeeRepository();

    @Test
    void employeesCannotBeDuplicated() {
        CompanyId companyId = CompanyId.of("Acme");
        EmployeeId employeeId = EmployeeId.of(123);

        repository.addEmployee(companyId, employeeId);
        repository.addEmployee(companyId, employeeId);

        assertThat(repository.findEmployeesOf(companyId)).containsExactly(employeeId);
    }

    @Test
    void canStoreMultipleEmployeesPerCompany() {
        CompanyId c1 = CompanyId.of("Company1");
        CompanyId c2 = CompanyId.of("Company1");
        assertThat(repository.findEmployeesOf(c1)).isEqualTo(emptyList());
        assertThat(repository.findEmployeesOf(c2)).isEqualTo(emptyList());

        EmployeeId e1 = EmployeeId.of(123);
        EmployeeId e2 = EmployeeId.of(345);
        EmployeeId e3 = EmployeeId.of(123);

        repository.addEmployee(c1, e1);
        repository.addEmployee(c1, e2);
        repository.addEmployee(c2, e3);

        assertThat(repository.findEmployeesOf(c1)).containsExactlyInAnyOrder(e1, e2);
        assertThat(repository.findEmployeesOf(c2)).containsExactly(e3);
    }

}