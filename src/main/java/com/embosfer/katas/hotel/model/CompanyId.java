package com.embosfer.katas.hotel.model;

public class CompanyId {
    private final String id;

    private CompanyId(String id) {
        this.id = id;
    }

    public static CompanyId of(String id) {
        return new CompanyId(id);
    }
}
