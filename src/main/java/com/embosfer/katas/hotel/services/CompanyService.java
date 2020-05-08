package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.caches.CompanyRepository;
import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;

public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void addEmployee(CompanyId companyId, EmployeeId employeeId) {
        if (companyRepository.findCompanyFor(employeeId).isPresent()) {
            throw new EmployeeAlreadyExistsException();
        }

        companyRepository.addEmployee(companyId, employeeId);
    }
}
