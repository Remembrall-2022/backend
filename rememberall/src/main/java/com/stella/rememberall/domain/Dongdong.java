package com.stella.rememberall.domain;

import javax.persistence.*;

@Entity
@Table(name = "dongdong")
public class Dongdong {
    @Id @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private Long point;

    private Long exp;

    @OneToOne
    @JoinColumn(name = "dongdong_img_id")
    private DongDongImg dongDongImg;
}
