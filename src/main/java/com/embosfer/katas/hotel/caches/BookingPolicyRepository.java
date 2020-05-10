package com.embosfer.katas.hotel.caches;

import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.EmployeeId;
import com.embosfer.katas.hotel.model.RoomType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyList;

public class BookingPolicyRepository {
    private final Map<CompanyId, Collection<RoomType>> roomsAllowedByCompany = new HashMap<>();
    private final Map<EmployeeId, Collection<RoomType>> roomsAllowedForEmployee = new HashMap<>();

    public void save(CompanyId companyId, Collection<RoomType> roomTypesAllowed) {
        roomsAllowedByCompany.put(companyId, roomTypesAllowed);
    }

    public void save(EmployeeId employeeId, Collection<RoomType> roomTypesAllowed) {
        roomsAllowedForEmployee.put(employeeId, roomTypesAllowed);
    }

    public Collection<RoomType> findRoomsAllowedFor(CompanyId companyId) {
        return roomsAllowedByCompany.getOrDefault(companyId, emptyList());
    }

    public Collection<RoomType> findRoomsAllowedFor(EmployeeId employeeId) {
        return roomsAllowedForEmployee.getOrDefault(employeeId, emptyList());
    }

    public void deletePolicyOf(EmployeeId employeeId) {
        roomsAllowedForEmployee.remove(employeeId);
    }
}
