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
    private Long placeLogId;
    private Place place;
    private UserLogImgResponseDto userLogImg;
    private String comment;

    public static PlaceLogResponseDto of(PlaceLog placeLog){
        return PlaceLogResponseDto.builder()
                .placeLogId(placeLog.getId())
                .place(placeLog.getPlace())
                .comment(placeLog.getComment())
                .build();
    }

//    이미지 여러개
//    public PlaceLogResponseDto updateUserLogImgListWithImgUrl(List<UserLogImgResponseDto> userLogImgList){
//        this.userLogImgList = userLogImgList;
//        return this;
//    }

    public PlaceLogResponseDto updateUserLogImgWithImgUrl(UserLogImgResponseDto userLogImg){
        this.userLogImg = userLogImg;
        return this;
    }

}
