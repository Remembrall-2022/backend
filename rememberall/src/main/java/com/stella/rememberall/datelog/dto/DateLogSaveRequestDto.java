package com.stella.rememberall.datelog.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.datelog.domain.WeatherInfo;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateLogSaveRequestDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull
    private LocalDate date;

    private WeatherInfo weatherInfo;

    private Long questionId;

    private String answer;

    @Builder
    public DateLogSaveRequestDto (LocalDate date, WeatherInfo weatherInfo, Long questionId, String answer){
        this.date = date;
        this.weatherInfo = weatherInfo;
        this.questionId = questionId;
        this.answer = answer;
    }

    public DateLog toEntity() {
        return DateLog.builder().build();
    }

}