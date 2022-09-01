package com.stella.rememberall.placelog;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class SpotResponseDto {

    private Long dateLogId;
    private Double longitude;
    private Double latitude;
    private int index;

    public static SpotResponseDto of(PlaceLog placeLog) {
        Place place = placeLog.getPlace();
        return new SpotResponseDto(placeLog.getDateLog().getId(), place.getLongitude(), place.getLatitude(), placeLog.getIndex());
    }
}
