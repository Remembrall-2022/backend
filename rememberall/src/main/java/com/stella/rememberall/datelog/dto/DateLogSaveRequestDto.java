package com.stella.rememberall.datelog.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.domain.Question;
import com.stella.rememberall.datelog.domain.WeatherInfo;
import com.stella.rememberall.tripLog.TripLog;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateLogSaveRequestDto {

    @Setter
    private TripLog tripLog;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotEmpty
    private LocalDate date;

    private WeatherInfo weatherInfo;

    private Question question;

    private String answer;

    @Builder
    public DateLogSaveRequestDto (TripLog tripLog, LocalDate date, WeatherInfo weatherInfo, Question question, String answer){
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