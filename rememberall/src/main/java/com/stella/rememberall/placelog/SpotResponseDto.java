package com.stella.rememberall.placelog;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class SpotResponseDto {

    private Long placeLogId;
    private Double longitude;
    private Double latitude;
    private int index;

    public static SpotResponseDto of(Long placeLogId, Double longitude, Double latitude, int index) {
        return new SpotResponseDto(placeLogId, longitude, latitude, index);
    }
}
