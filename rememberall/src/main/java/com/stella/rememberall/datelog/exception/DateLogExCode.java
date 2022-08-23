package com.stella.rememberall.datelog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateLogExCode {

    DUPLICATED_DATELOG("이미 같은 일기가 존재합니다."),
    DATELOG_NOT_FOUND("해당 일기를 찾을 수 없습니다."),
    COUNT_EXCEED("관광지 일기는 최대 10개까지 쓸 수 있습니다."),
    COUNT_NOT_MATCH("이미지 파일 개수와 관광지 일기 개수가 일치하지 않습니다."),
    NO_AUTHORIZATION("접근 권한이 없습니다."),
    DATELOG_NOT_BELONG_TO_TRIPLOG("해당 날짜별 일기는 해당 일기장에 속하지 않습니다.");

    private String defaultMessage;
}
