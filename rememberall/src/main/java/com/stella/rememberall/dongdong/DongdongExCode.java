package com.stella.rememberall.dongdong;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DongdongExCode {

    DONGDONG_IMG_NOT_FOUND("둥둥이 이미지를 찾을 수 없습니다.");

    private String defaultMessage;
}
