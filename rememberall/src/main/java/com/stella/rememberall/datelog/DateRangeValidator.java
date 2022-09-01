package com.stella.rememberall.datelog;

import java.time.LocalDate;

public class DateRangeValidator {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateRangeValidator(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isWithinRange(LocalDate testDate) {
        return (testDate.isEqual(startDate) || testDate.isEqual(endDate))
                || (testDate.isBefore(endDate) && testDate.isAfter(startDate));
    }

}