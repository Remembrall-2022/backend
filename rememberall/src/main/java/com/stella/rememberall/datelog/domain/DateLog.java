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

@Table(name = "date_log")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateLog extends BaseTimeEntity {
    @Id @Getter
    @Column(name = "date_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private LocalDate date;

    @Embedded @Getter
    @Column(name = "weather_info")
    private WeatherInfo weatherInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @Getter
    private Question question;

    @Getter
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_log_id")
    private TripLog tripLog;

    /**
     * 양방향 연관관계 편의 메서드 -> PlaceLog 저장할 때
     * 길이 10제한 -> 어떻게 구현할까?
     * */
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
