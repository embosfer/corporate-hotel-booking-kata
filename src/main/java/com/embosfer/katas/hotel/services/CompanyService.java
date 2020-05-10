package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.caches.BookingPolicyRepository;
import com.embosfer.katas.hotel.caches.BookingRepository;
import com.embosfer.katas.hotel.caches.CompanyRepository;
import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;

public class CompanyService {

    private final CompanyRepository companyRepository;
    private final BookingPolicyRepository bookingPolicyRepository;
    private final BookingRepository bookingRepository;

    public CompanyService(CompanyRepository companyRepository, BookingPolicyRepository bookingPolicyRepository, BookingRepository bookingRepository) {
        this.companyRepository = companyRepository;
        this.bookingPolicyRepository = bookingPolicyRepository;
        this.bookingRepository = bookingRepository;
    }

    public void addEmployee(CompanyId companyId, EmployeeId employeeId) {
        if (companyRepository.findCompanyFor(employeeId).isPresent()) {
            throw new EmployeeAlreadyExistsException();
        }

        companyRepository.addEmployee(companyId, employeeId);
    }

    public void deleteEmployee(EmployeeId employeeId) {
        companyRepository.deleteEmployee(employeeId);
        bookingPolicyRepository.deletePolicyOf(employeeId);
        bookingRepository.deleteBookingsOf(employeeId);
    }
}
