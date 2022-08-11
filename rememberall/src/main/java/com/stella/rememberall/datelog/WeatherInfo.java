package com.stella.rememberall.datelog;

import javax.persistence.Embeddable;

@Embeddable
public class WeatherInfo {
    private String weather;
    private int degree;
}
