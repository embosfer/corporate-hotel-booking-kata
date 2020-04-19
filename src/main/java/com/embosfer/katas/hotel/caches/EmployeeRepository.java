package com.embosfer.katas.hotel.caches;

import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EmployeeRepository {

    private Map<CompanyId, Collection<EmployeeId>> employees = new HashMap<>();

    public void addEmployee(CompanyId companyId, EmployeeId employeeId) {
        employees.compute(companyId, (company, employeeIds) -> {
            if (employeeIds == null) {
                employeeIds = new HashSet<>();
            }
            employeeIds.add(employeeId);
            return employeeIds;
        });
    }

    public Collection<EmployeeId> employeesOf(CompanyId companyId) {
        return employees.get(companyId);
    }
}
