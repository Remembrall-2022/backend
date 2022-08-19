package com.stella.rememberall.userLogImg;

import com.stella.rememberall.domain.PlaceLog;

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

    private int index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_log_id")
    private PlaceLog placeLog;

}
