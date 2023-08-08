package com.stella.rememberall.userLogImg;

import com.stella.rememberall.placelog.PlaceLog;

public class UserLogImgSaveRequestVO {
    public static UserLogImg of(String url, int index, PlaceLog placeLog) {
        return UserLogImg.builder()
                .index(index)
                .fileKey(url)
                .placeLog(placeLog)
                .build();
    }
}
