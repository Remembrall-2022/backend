package com.stella.rememberall.datelog.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionCategoryName {
    BEFORE("before"),
    PROGRESS("progress"),
    AFTER("after"),
    THOUGHT("thought");

    @JsonValue
    private String value;

}
