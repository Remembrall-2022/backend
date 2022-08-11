package com.stella.rememberall.dongdong;

import javax.persistence.*;

@Table(name = "dongdong_img")
@Entity
public class DongdongImg {
    @Id
    @Column(name = "dongdong_img_id")
    private Long id;

    @Column(name = "dongdong_img_url")
    private String url;

}
