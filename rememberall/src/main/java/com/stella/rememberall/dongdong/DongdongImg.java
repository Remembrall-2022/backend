package com.stella.rememberall.dongdong;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "dongdong_img")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DongdongImg {
    @Id
    @Column(name = "dongdong_img_id")
    private Long id;

    @Column(name = "dongdong_img_url")
    private String url;

}
