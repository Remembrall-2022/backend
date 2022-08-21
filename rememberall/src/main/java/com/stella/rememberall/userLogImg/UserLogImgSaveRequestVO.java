package com.stella.rememberall.userLogImg;

public class UserLogImgSaveRequestVO {
    public static UserLogImg of(String url, int index) {
        return UserLogImg.builder()
                .index(index)
                .imgUrl(url)
                .build();
    }
}
