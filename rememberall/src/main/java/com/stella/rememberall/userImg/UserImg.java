package com.stella.rememberall.userImg;

import com.stella.rememberall.domain.ItemWithUserImg;
import com.stella.rememberall.domain.PlaceLog;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "user_img")
@Entity
public class UserImg {
    @Id
    @Column(name = "user_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_log_id")
    private PlaceLog placeLog;

    @OneToMany(mappedBy = "userImg")
    private List<ItemWithUserImg> itemWithUserImgList = new ArrayList<>();

}
