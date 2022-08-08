package com.stella.rememberall.tripLog.dto;

import com.stella.rememberall.tripLog.TripLog;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TripLogResponseDto {
    private Long triplogId;
    private String title;
    private LocalDate tripStartDate;
    private LocalDate tripEndDate;
    @Setter private String tripLogImgUrl;

    public static TripLogResponseDto of(TripLog tripLog) {
        return new TripLogResponseDto(tripLog);
    }

    public TripLogResponseDto(TripLog tripLog){
        this.triplogId = tripLog.getId();
        this.title = tripLog.getTitle();
        this.tripStartDate = tripLog.getTripStartDate();
        this.tripEndDate = tripLog.getTripEndDate();
    }


}
