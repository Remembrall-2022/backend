package com.stella.rememberall.placelog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceLogSaveRequestDto {
    private PlaceSaveRequestDto placeInfo;
    private String comment;

    public PlaceLog toEntity(){
        return PlaceLog.builder()
                .place(placeInfo.toEntity())
                .comment(comment)
                .build();
    }

    public PlaceLog toEntityWithId(Long placeLogId){
        return PlaceLog.builder()
                .id(placeLogId)
                .place(placeInfo.toEntity())
                .comment(comment)
                .build();
    }

}
