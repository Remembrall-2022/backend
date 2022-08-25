package com.stella.rememberall.datelog.domain;

import com.stella.rememberall.domain.BaseTimeEntity;
import com.stella.rememberall.placelog.PlaceLog;
import com.stella.rememberall.tripLog.TripLog;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "date_log")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateLog extends BaseTimeEntity {
    @Id
    @Column(name = "date_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Embedded
    @Column(name = "weather_info")
    private WeatherInfo weatherInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_log_id")
    private TripLog tripLog;

    @OneToMany(mappedBy = "dateLog")
    private List<PlaceLog> placeLogList = new ArrayList<>();

    @Builder
    public DateLog(TripLog tripLog, LocalDate date, WeatherInfo weatherInfo, Question question, String answer, List<PlaceLog> placeLogList) {
        this.tripLog = tripLog;
        this.date = date;
        this.weatherInfo = weatherInfo;
        this.question = question;
        this.answer = answer;
        this.placeLogList = placeLogList;
    }

}
