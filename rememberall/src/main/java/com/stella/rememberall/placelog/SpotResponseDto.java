package com.stella.rememberall.placelog;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class SpotResponseDto {

    private Long placeId;
    private Double longitude;
    private Double latitude;
    private int index;

    public static SpotResponseDto of(Long placeId, Double longitude, Double latitude, int index) {
        return new SpotResponseDto(placeId, longitude, latitude, index);
    }
}
