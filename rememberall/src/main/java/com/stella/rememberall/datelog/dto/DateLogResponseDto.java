package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.WeatherInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DateLogResponseDto {

    private LocalDate date;

    private WeatherInfo weatherInfo;

    private Question question;

    private String answer;

    /**
     * TODO: PlaceLog 리스트로 불러와야 하는지 확인
     * */

    @Builder
    public DateLogResponseDto(LocalDate date, WeatherInfo weatherInfo, Question question, String answer) {
        this.date = date;
        this.weatherInfo = weatherInfo;
        this.question = question;
        this.answer = answer;
    }
}
