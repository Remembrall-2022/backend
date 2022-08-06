package com.stella.rememberall.domain;

import javax.persistence.Embeddable;

@Embeddable
public class WeatherInfo {
    private String weather;
    private int degree;
}
