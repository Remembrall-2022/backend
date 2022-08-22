package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "dongdong")
@NoArgsConstructor
@Getter
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
    private DongdongImg dongDongImg;

    public Dongdong (User user) {
        this.user = user;
        this.point = 0L;
        this.exp = 0L;
    }
}