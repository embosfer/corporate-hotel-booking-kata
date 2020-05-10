package com.embosfer.katas.hotel.caches;

import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;

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
