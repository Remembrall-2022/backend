package com.stella.rememberall.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "user")
@Entity
public class User {

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 300, nullable = false)
    private String password;

    @Column(name="kakao_id")
    private Long kakaoId;

    @Column(length = 300, nullable = false)
    private String name;

    @Column(name="term_agree")
    private Boolean termAgree;

    @Column(name="alarm_agree")
    private Boolean alarmAgree;

    @Column(name="register_date")
    private LocalDateTime registerDate;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    //@JoinColumn(name="dongdong_id")
    private Dongdong dongdong;

    @OneToMany(mappedBy = "user")
    private List<TripLog> tripLogList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ItemPurchasedByUser> itemPurchasedByUserList = new ArrayList<>();

}
