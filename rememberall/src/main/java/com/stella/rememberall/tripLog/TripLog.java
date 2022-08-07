package com.stella.rememberall.tripLog;

import com.stella.rememberall.domain.BaseTimeEntity;
import com.stella.rememberall.domain.DateLog;
import com.stella.rememberall.user.User;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "trip_log")
@Entity
public class TripLog extends BaseTimeEntity {
    @Id
    @Column(name = "trip_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    //Column(name="create_date")
    //private LocalDateTime createDate;

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
    public TripLog(String title, LocalDate tripStartDate, LocalDate tripEndDate, User user){
        this.title = title;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.user = user;
    }

}
