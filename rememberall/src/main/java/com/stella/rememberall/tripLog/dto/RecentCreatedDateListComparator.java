package com.stella.rememberall.tripLog.dto;

import com.stella.rememberall.tripLog.TripLog;

import java.time.LocalDateTime;
import java.util.Comparator;

public class RecentCreatedDateListComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        LocalDateTime createdDate1 = ((TripLog) o1).getCreatedDate();
        LocalDateTime createdDate2 = ((TripLog) o2).getCreatedDate();

        if (createdDate1.isAfter(createdDate2))
            return -1;
        else if (createdDate1.isEqual(createdDate2))
            return 0;
        else
            return 1;

    }
}
