package com.embosfer.katas.hotel.company;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CompanyRepositoryTest {

    CompanyRepository repository = new CompanyRepository();

    @Test
    void canStoreMultipleEmployeesPerCompany() {
        CompanyId c1 = CompanyId.of("Company1");
        CompanyId c2 = CompanyId.of("Company2");

        EmployeeId e1 = EmployeeId.of(123);
        EmployeeId e2 = EmployeeId.of(345);
        EmployeeId e3 = EmployeeId.of(678);

        assertThat(repository.findCompanyFor(e1)).isEmpty();
        assertThat(repository.findCompanyFor(e2)).isEmpty();
        assertThat(repository.findCompanyFor(e3)).isEmpty();

        repository.addEmployee(c1, e1);
        repository.addEmployee(c1, e2);
        repository.addEmployee(c2, e3);

        assertThat(repository.findCompanyFor(e1)).isEqualTo(Optional.of(c1));
        assertThat(repository.findCompanyFor(e2)).isEqualTo(Optional.of(c1));
        assertThat(repository.findCompanyFor(e3)).isEqualTo(Optional.of(c2));
    }

    @Test
    void canDeleteEmployees() {
        CompanyId c1 = CompanyId.of("Company1");
        EmployeeId e1 = EmployeeId.of(123);
        EmployeeId e2 = EmployeeId.of(456);

        repository.addEmployee(c1, e1);
        repository.addEmployee(c1, e2);
        repository.deleteEmployee(e1);

        assertThat(repository.findCompanyFor(e1)).isEqualTo(Optional.empty());
        assertThat(repository.findCompanyFor(e2)).isEqualTo(Optional.of(c1));

        repository.deleteEmployee(e2);
        assertThat(repository.findCompanyFor(e2)).isEqualTo(Optional.empty());
    }

}