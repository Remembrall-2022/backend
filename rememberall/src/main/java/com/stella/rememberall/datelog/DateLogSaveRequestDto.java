package com.stella.rememberall.datelog;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.stella.rememberall.datelog.Question;
import com.stella.rememberall.datelog.WeatherInfo;
import com.stella.rememberall.tripLog.TripLog;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;

import java.time.LocalDate;

@Getter
public class DateLogSaveRequestDto {

    private TripLog tripLog;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    private WeatherInfo weatherInfo;

    private Question question;

    private String answer;

    @Builder
    public DateLogSaveRequestDto(TripLog tripLog, LocalDate date, WeatherInfo weatherInfo, Question question, String answer) {
        Assert.notNull(tripLog, "TripLog must not be null");
        Assert.notNull(date, "Date must not be null");

        this.tripLog = tripLog;
        this.date = date;
        this.weatherInfo = weatherInfo;
        this.question = question;
        this.answer = answer;
    }

    public DateLog toEntity() {
        return DateLog.builder()
                .tripLog(tripLog)
                .date(date)
                .weatherInfo(weatherInfo)
                .question(question)
                .answer(answer)
                .build();
    }

}