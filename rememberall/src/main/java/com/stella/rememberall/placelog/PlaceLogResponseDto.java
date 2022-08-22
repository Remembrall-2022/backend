package com.stella.rememberall.placelog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceLogResponseDto {
    private Place place;
    private String comment;

    public static PlaceLogResponseDto of(PlaceLog placeLog){
        return PlaceLogResponseDto.builder()
                .place(placeLog.getPlace())
                .comment(placeLog.getComment())
                .build();
    }
}
