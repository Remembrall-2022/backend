package com.stella.rememberall.tripLog.dto;

import com.stella.rememberall.tripLog.TripLog;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TripLogSimpleResponseDto {
    private Long triplogId;
    private String title;
    private LocalDate tripStartDate;
    private LocalDate tripEndDate;
    @Setter private String tripLogImgUrl;

    public static TripLogSimpleResponseDto of(TripLog tripLog) {
        return new TripLogSimpleResponseDto(tripLog);
    }

    public TripLogSimpleResponseDto(TripLog tripLog){
        this.triplogId = tripLog.getId();
        this.title = tripLog.getTitle();
        this.tripStartDate = tripLog.getTripStartDate();
        this.tripEndDate = tripLog.getTripEndDate();
        this.tripLogImgUrl = "bucketsampleimage.jpg";
    }


}
