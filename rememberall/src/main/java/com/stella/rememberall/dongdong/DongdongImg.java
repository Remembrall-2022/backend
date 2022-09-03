package com.stella.rememberall.dongdong;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DongdongImg {
    // todo : 동동이 이미지 나오면 여기에 파일 key 넣어야함
    STEP0("dongdong-image/dongdong.png"),
    STEP1("dongdong-image/dongdong.png"),
    STEP2("dongdong-image/dongdong.png"),
    STEP3("dongdong-image/dongdong.png");

    private String imgKey;
}
