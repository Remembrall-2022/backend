package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.WeatherInfo;
import com.stella.rememberall.domain.PlaceLog;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class DateLogResponseDto {

    private LocalDate date;

    private WeatherInfo weatherInfo;

    private Question question;

    private String answer;

    private List<PlaceLog> placeLogList;

    @Builder
    public DateLogResponseDto(LocalDate date, WeatherInfo weatherInfo, Question question, String answer, List<PlaceLog> placeLogList) {
        this.date = date;
        this.weatherInfo = weatherInfo;
        this.question = question;
        this.answer = answer;
        this.placeLogList = placeLogList;
    }
}
