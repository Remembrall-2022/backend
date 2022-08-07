package com.stella.rememberall.domain;

import com.stella.rememberall.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "trip_log")
@Entity
public class TripLog {
    @Id
    @Column(name = "trip_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name="create_date")
    private LocalDateTime createDate;

    @Column(name="trip_start_date")
    private LocalDateTime tripStartDate;

    @Column(name="trip_end_date")
    private LocalDateTime tripEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "tripLog")
    private List<DateLog> dateLogList = new ArrayList<>();

}
