package com.stella.rememberall.tripLog.dto;

import com.stella.rememberall.datelog.domain.DateLog;

import java.time.LocalDate;
import java.util.Comparator;

public class OldCreatedDateListComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        LocalDate createdDate1 = ((DateLog) o1).getDate();
        LocalDate createdDate2 = ((DateLog) o2).getDate();

        if (createdDate1.isBefore(createdDate2))
            return -1;
        else if (createdDate1.isEqual(createdDate2))
            return 0;
        else
            return 1;
    }
}
