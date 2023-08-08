package com.stella.rememberall.tripLog;

import com.stella.rememberall.domain.BaseTimeEntity;
import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(name = "trip_log")
@Entity
public class TripLog extends BaseTimeEntity {
    @Id
    @Column(name = "trip_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name="trip_start_date")
    private LocalDate tripStartDate;

    @Column(name="trip_end_date")
    private LocalDate tripEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "tripLog")
    private List<DateLog> dateLogList = new ArrayList<>();

    @Builder
    public TripLog(Long id, String title, LocalDate tripStartDate, LocalDate tripEndDate, User user){
        this.id = id;
        this.title = title;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.user = user;
    }

}
