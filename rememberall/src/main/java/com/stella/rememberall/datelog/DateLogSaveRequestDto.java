package com.stella.rememberall.datelog;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.stella.rememberall.tripLog.TripLog;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Builder(builderMethodName = "innerBuilder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DateLogSaveRequestDto {

    @NotEmpty
    private TripLog tripLog;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotEmpty
    private LocalDate date;

    private WeatherInfo weatherInfo;

    private Question question;

    private String answer;

    public static DateLogSaveRequestDtoBuilder builder(TripLog tripLog, LocalDate date) {
        return innerBuilder()
                .tripLog(tripLog)
                .date(date);
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