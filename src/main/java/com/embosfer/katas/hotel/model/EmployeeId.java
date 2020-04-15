package com.embosfer.katas.hotel.model;

public class EmployeeId {
    private final int employeeId;

    private EmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public static EmployeeId of(int employeeId) {
        return new EmployeeId(employeeId);
    }
}
