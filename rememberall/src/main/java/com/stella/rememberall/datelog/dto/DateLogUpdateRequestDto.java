package com.stella.rememberall.datelog.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.stella.rememberall.datelog.domain.WeatherInfo;
import com.stella.rememberall.placelog.PlaceLogUpdateRequestDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateLogUpdateRequestDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull
    private LocalDate date;

    private WeatherInfo weatherInfo;

    private Long questionId;

    private String answer;

    private ArrayList<PlaceLogUpdateRequestDto> placeLogList;

}
