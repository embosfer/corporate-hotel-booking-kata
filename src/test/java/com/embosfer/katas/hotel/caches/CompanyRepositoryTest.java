package com.embosfer.katas.hotel.caches;

import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class CompanyRepositoryTest {

    CompanyRepository repository = new CompanyRepository();

    @Test
    void canStoreMultipleEmployeesPerCompany() {
        CompanyId c1 = CompanyId.of("Company1");
        CompanyId c2 = CompanyId.of("Company1");

        EmployeeId e1 = EmployeeId.of(123);
        EmployeeId e2 = EmployeeId.of(345);
        EmployeeId e3 = EmployeeId.of(123);

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

}