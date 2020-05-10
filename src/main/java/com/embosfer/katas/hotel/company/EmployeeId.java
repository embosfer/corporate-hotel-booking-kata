package com.embosfer.katas.hotel.company;

import java.util.Objects;

public class EmployeeId {
    private final int employeeId;

    private EmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public static EmployeeId of(int employeeId) {
        return new EmployeeId(employeeId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeId that = (EmployeeId) o;
        return employeeId == that.employeeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }
}
