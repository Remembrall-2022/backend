package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.WeatherInfo;
import com.stella.rememberall.tripLog.TripLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DateLogSaveRequestVo {

    private LocalDate date;
    private WeatherInfo weatherInfo;
    @Setter private Question question;
    private String answer;
    @Setter private TripLog tripLog;

    public DateLogSaveRequestVo(DateLogSaveRequestDto requestDto){
        this.date = requestDto.getDate();
        this.weatherInfo = requestDto.getWeatherInfo();
        this.answer = requestDto.getAnswer();
    }

    public DateLog toEntity() {
        return DateLog.builder()
                .date(date)
                .weatherInfo(weatherInfo)
                .answer(answer)
                .question(question)
                .tripLog(tripLog)
                .placeLogList(new ArrayList<>())
                .build();
    }

}
