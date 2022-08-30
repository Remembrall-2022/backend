package com.stella.rememberall.placelog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceLogSaveRequestDto {
    private PlaceSaveRequestDto placeInfo;
    private String comment;
    private String imgName;

    public PlaceLog toEntity(){
        return PlaceLog.builder()
                .place(placeInfo.toEntity())
                .comment(comment)
                .userLogImgList(new ArrayList<>())
                .build();
    }

    public PlaceLog toEntityWithId(Long placeLogId){
        return PlaceLog.builder()
                .id(placeLogId)
                .place(placeInfo.toEntity())
                .comment(comment)
                .userLogImgList(new ArrayList<>())
                .build();
    }

}
