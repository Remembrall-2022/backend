package com.stella.rememberall.datelog.dto;

import com.stella.rememberall.datelog.domain.WeatherInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WeatherInfoUpdateRequestDto {
    private WeatherInfo weatherInfo;
}
