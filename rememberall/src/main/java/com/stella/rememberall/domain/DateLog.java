package com.stella.rememberall.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "date_log")
@Entity
public class DateLog {
    @Id
    @Column(name = "date_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="create_date")
    private LocalDateTime createDate;

    @Column(name="update_date")
    private LocalDateTime updateDate;

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

}
