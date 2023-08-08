package com.stella.rememberall.datelog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateLogExCode {

    DUPLICATED_DATELOG("이미 같은 일기가 존재합니다."),
    DUPLICATED_PLACEID("하나의 날짜별 일기 안에서 중복된 관광지를 요청할 수 없습니다."),
    DUPLICATED_PLACE_INDEX("관광지별 일기의 인덱스가 중복되어 수정할 수 없습니다."),
    DATELOG_NOT_FOUND("해당 일기를 찾을 수 없습니다."),
    COUNT_EXCEED("관광지 일기는 최대 10개까지 쓸 수 있습니다."),
    COUNT_NOT_MATCH("이미지 파일 개수와 관광지 일기 개수가 일치하지 않습니다."),
    NO_AUTHORIZATION("접근 권한이 없습니다."),
    DATELOG_NOT_BELONG_TO_TRIPLOG("해당 날짜별 일기는 해당 일기장에 속하지 않습니다."),
    INVALID_DATE("요청한 날짜가 일기장에 설정된 시작 날짜와 끝 날짜를 벗어납니다."),
    INDEX_ERROR("요청한 인덱스에 문제가 있습니다."),
    COMMENT_LENGTH_EXCEED("코멘트는 255자 이하만 작성 가능합니다.");

    private String defaultMessage;
}
