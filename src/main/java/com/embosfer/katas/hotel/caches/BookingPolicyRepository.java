package com.embosfer.katas.hotel.caches;

import com.embosfer.katas.hotel.model.CompanyId;
import com.embosfer.katas.hotel.model.RoomType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyList;

public class BookingPolicyRepository {
    private Map<CompanyId, Collection<RoomType>> roomsAllowed = new HashMap<>();

    public void save(CompanyId companyId, Collection<RoomType> roomTypesAllowed) {
        roomsAllowed.put(companyId, roomTypesAllowed);
    }

    public Collection<RoomType> findRoomsAllowedFor(CompanyId companyId) {
        return roomsAllowed.getOrDefault(companyId, emptyList());
    }
}
