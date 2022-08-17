package com.stella.rememberall.domain;

import com.stella.rememberall.userLogImg.UserLogImg;

import javax.persistence.*;

@Table(name = "item_with_user_img")
@Entity
public class ItemWithUserImg {
    @Id
    @Column(name = "item_with_user_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer x;

    private Integer y;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_img_id")
    private UserLogImg userLogImg;

}
