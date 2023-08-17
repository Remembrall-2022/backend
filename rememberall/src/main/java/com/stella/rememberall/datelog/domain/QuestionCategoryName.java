package com.stella.rememberall.datelog.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum QuestionCategoryName {
    BEFORE("before", "여행 준비"),
    PROGRESS("progress", "여행 당일"),
    AFTER("after", "여행 마무리"),
    THOUGHT("thought", "기타");

    @JsonValue
    private String value;
    private String kor;

    public static QuestionCategoryName of(String input) {
        return Arrays.stream(QuestionCategoryName.values())
                .filter(category -> category.kor.equals(input))
                .findAny()
                .orElseThrow(() -> new RuntimeException("해당 카테고리를 찾을 수 없습니다."));
    }

}
