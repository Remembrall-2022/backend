package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.WeatherInfo;
import com.stella.rememberall.tripLog.TripLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DateLogSaveRequestVo {

    private LocalDate date;
    private WeatherInfo weatherInfo;
    private Question question;
    private String answer;
    private TripLog tripLog;

    public DateLog toEntity() {
        return DateLog.builder()
                .date(date)
                .weatherInfo(weatherInfo)
                .answer(answer)
                .question(question)
                .tripLog(tripLog)
                .build();
    }

}
