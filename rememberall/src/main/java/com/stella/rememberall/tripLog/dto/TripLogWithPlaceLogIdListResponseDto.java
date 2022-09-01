package com.stella.rememberall.tripLog.dto;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.tripLog.TripLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TripLogWithPlaceLogIdListResponseDto {
    // 일기장 정보
    private Long triplogId;
    private String title;
    private LocalDate tripStartDate;
    private LocalDate tripEndDate;

    // PlaceLogId
    private List<Long> placeLogIdList;

    public static TripLogWithPlaceLogIdListResponseDto of(TripLog tripLog) {
        return TripLogWithPlaceLogIdListResponseDto.builder()
                .triplogId(tripLog.getId())
                .title(tripLog.getTitle())
                .tripStartDate(tripLog.getTripStartDate())
                .tripEndDate(tripLog.getTripEndDate())
                .placeLogIdList(getPlaceLogIdList(tripLog))
                .build();
    }

    private static List<Long> getPlaceLogIdList(TripLog tripLog) {
        Collections.sort(tripLog.getDateLogList(), new OldCreatedDateListComparator());
        List<Long> placeLogIdList = new ArrayList<>();
        for (DateLog dateLog : tripLog.getDateLogList()) {
            placeLogIdList.add(dateLog.getId());
        }
        return placeLogIdList;
    }


}
