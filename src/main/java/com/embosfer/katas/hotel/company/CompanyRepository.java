package com.embosfer.katas.hotel.company;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CompanyRepository {

    private final Map<EmployeeId, CompanyId> employees = new HashMap<>();

    public void addEmployee(CompanyId companyId, EmployeeId employeeId) {
        employees.put(employeeId, companyId);
    }

    public void deleteEmployee(EmployeeId employeeId) {
        employees.remove(employeeId);
    }

    public Optional<CompanyId> findCompanyFor(EmployeeId employeeId) {
        return Optional.ofNullable(employees.get(employeeId));
    }
}
