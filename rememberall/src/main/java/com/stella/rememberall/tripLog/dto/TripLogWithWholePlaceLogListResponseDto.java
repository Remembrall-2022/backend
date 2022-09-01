package com.stella.rememberall.tripLog.dto;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.dto.DateLogResponseDto;
import com.stella.rememberall.tripLog.TripLog;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TripLogWithWholePlaceLogListResponseDto {
    // 일기장 정보
    private Long triplogId;
    private String title;
    private LocalDate tripStartDate;
    private LocalDate tripEndDate;

    // PlaceLog
    @Setter private List<DateLogResponseDto> dateLogResponseDtoList;

    public static TripLogWithWholePlaceLogListResponseDto of(TripLog tripLog) {
        return TripLogWithWholePlaceLogListResponseDto.builder()
                .triplogId(tripLog.getId())
                .title(tripLog.getTitle())
                .tripStartDate(tripLog.getTripStartDate())
                .tripEndDate(tripLog.getTripEndDate())
                .build();
    }
}
