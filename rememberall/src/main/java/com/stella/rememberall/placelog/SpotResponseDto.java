package com.stella.rememberall.placelog;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


@Getter
@AllArgsConstructor
public class SpotResponseDto {

    private Long dateLogId;
    private Long placeId;
    private Double longitude;
    private Double latitude;
    private int index;

    public static SpotResponseDto of(PlaceLog placeLog) {
        Place place = placeLog.getPlace();
        return new SpotResponseDto(placeLog.getDateLog().getId(), place.getId(), place.getLongitude(), place.getLatitude(), placeLog.getIndex());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpotResponseDto) {
            return this.placeId == ((SpotResponseDto)obj).getPlaceId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return placeId.hashCode();
    }
}
