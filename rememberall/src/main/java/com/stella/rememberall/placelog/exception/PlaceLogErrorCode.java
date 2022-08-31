package com.stella.rememberall.placelog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlaceLogErrorCode {
    NOT_FOUND("존재하지 않는 관광지 정보입니다."),
    USER_LOG_IMG_NOT_BELONG_TO_PLACE_LOG("요청한 이미지는 해당 관광지별 일기에 속하지 않습니다.");

    private String defaultErrorMessage;
}
