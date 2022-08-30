package com.stella.rememberall.datelog.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionCategoryName {
    BEFORE("여행 전"),
    PROGRESS("여행 중"),
    AFTER("여행 후"),
    THOUGHT("여행에 대한 내 생각");

    private String value;

}
