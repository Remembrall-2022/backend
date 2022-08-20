package com.stella.rememberall.userLogImg;

import com.stella.rememberall.domain.PlaceLog;
import lombok.Builder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "user_img")
@Entity
public class UserLogImg {
    @Id
    @Column(name = "user_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int index;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_log_id")
    private PlaceLog placeLog;

    @Builder
    public UserLogImg(int index, String imgUrl, PlaceLog placeLog){
        this.index = index;
        this.imgUrl = imgUrl;
        this.placeLog = placeLog;
    }

}
