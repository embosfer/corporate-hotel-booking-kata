package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.caches.EmployeeRepository;
import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;

public class CompanyService {

    private final EmployeeRepository employeeRepository;

    public CompanyService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void addEmployee(CompanyId companyId, EmployeeId employeeId) {
        if (employeeRepository.findEmployeesOf(companyId)
                .stream()
                .anyMatch(employee -> employee.equals(employeeId))) {
            throw new EmployeeAlreadyExistsException();
        }

        employeeRepository.addEmployee(companyId, employeeId);
    }
}
