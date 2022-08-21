package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.datelog.domain.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DateLogResponseDto {

    private LocalDate date;

    private String weather;

    private Integer degree;

    private String questionTopic;

    private String answer;

    @Builder
    public DateLogResponseDto(LocalDate date, String weather, Integer degree, String questionTopic, String answer) {
        this.date = date;
        this.weather = weather;
        this.degree = degree;
        this.questionTopic = questionTopic;
        this.answer = answer;
    }
}
