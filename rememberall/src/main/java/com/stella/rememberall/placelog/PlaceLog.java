package com.stella.rememberall.placelog;

import com.stella.rememberall.domain.DateLog;
import com.stella.rememberall.placelog.Place;
import com.stella.rememberall.userLogImg.UserLogImg;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
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
    private List<UserLogImg> userLogImgList = new ArrayList<>();

    @Builder
    public PlaceLog(Long id, String comment, DateLog dateLog, Place place, List<UserLogImg> userLogImgList){
        this.id = id;
        this.comment = comment;
        this.dateLog = dateLog;
        this.place = place;
        this.userLogImgList = userLogImgList;
    }

    public PlaceLog updateComment(String comment) {
        this.comment = comment;
        return this;
    }

}
