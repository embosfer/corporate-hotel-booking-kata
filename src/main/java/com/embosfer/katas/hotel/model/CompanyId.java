package com.embosfer.katas.hotel.model;

import java.util.Objects;

public class CompanyId {
    private final String id;

    private CompanyId(String id) {
        this.id = id;
    }

    public static CompanyId of(String id) {
        return new CompanyId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyId companyId = (CompanyId) o;
        return id.equals(companyId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
