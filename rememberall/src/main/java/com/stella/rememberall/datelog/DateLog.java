package com.stella.rememberall.datelog;

import com.stella.rememberall.domain.PlaceLog;
import com.stella.rememberall.tripLog.TripLog;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "date_log")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateLog {
    @Id
    @Column(name = "date_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name="create_date")
//    private LocalDateTime createDate;
//
//    @Column(name="update_date")
//    private LocalDateTime updateDate;

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
    public DateLog(TripLog tripLog, LocalDate date, WeatherInfo weatherInfo, Question question, String answer) {
        Assert.notNull(tripLog, "TripLog must not be null");
        Assert.notNull(date, "Date must not be null");

        this.tripLog = tripLog;
        this.date = date;
        this.weatherInfo = weatherInfo;
        this.question = question;
        this.answer = answer;
    }

}
