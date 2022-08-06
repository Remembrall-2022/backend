package com.stella.rememberall.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "place_log")
@Entity
public class PlaceLog {
    @Id
    @Column(name = "place_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_log_id")
    private DateLog dateLog;

    @OneToOne @JoinColumn(name = "place_id")
    private Place place;

    @OneToMany(mappedBy = "placeLog")
    private List<UserImg> userImgList = new ArrayList<>();

}
