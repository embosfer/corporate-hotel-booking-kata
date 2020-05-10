package com.embosfer.katas.hotel.booking;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatesValidatorTest {

    DatesValidator datesValidator = new DatesValidator();

    @Test
    void returnsFalseIfCheckInDateIsSameAsCheckOutDate() {

        assertFalse(datesValidator.validate(LocalDate.EPOCH, LocalDate.EPOCH));
    }

    @Test
    void returnsFalseIfCheckInDateIsAfterCheckOutDate() {

        assertFalse(datesValidator.validate(LocalDate.EPOCH.plus(1, DAYS), LocalDate.EPOCH));
    }

    @Test
    void returnsTrueIfCheckInDateIsBeforeCheckOutDate() {

        assertTrue(datesValidator.validate(LocalDate.EPOCH.minus(1, DAYS), LocalDate.EPOCH));
    }
}