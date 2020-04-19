package com.embosfer.katas.hotel.services;

import java.time.Duration;
import java.time.LocalDate;

public class DatesValidator {

    public boolean validate(LocalDate checkIn, LocalDate checkOut) {
        return Duration.between(checkIn.atStartOfDay(), checkOut.atStartOfDay()).toDays() >= 1;
    }
}
