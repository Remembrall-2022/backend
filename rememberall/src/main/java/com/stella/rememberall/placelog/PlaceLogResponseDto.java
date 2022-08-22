package com.stella.rememberall.placelog;

import com.stella.rememberall.userLogImg.UserLogImg;
import com.stella.rememberall.userLogImg.UserLogImgResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceLogResponseDto {
    private Place place;
    private List<UserLogImgResponseDto> userLogImgList;
    private String comment;

    public static PlaceLogResponseDto of(PlaceLog placeLog){
        return PlaceLogResponseDto.builder()
                .place(placeLog.getPlace())
                .comment(placeLog.getComment())
                .build();
    }

    public PlaceLogResponseDto updateUserLogImgListWithImgUrl(List<UserLogImgResponseDto> userLogImgList){
        this.userLogImgList = userLogImgList;
        return this;
    }
}
