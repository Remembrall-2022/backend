package com.stella.rememberall.user.domain;

import com.stella.rememberall.dongdong.Dongdong;
import com.stella.rememberall.domain.AuthType;
import com.stella.rememberall.domain.BaseTimeEntity;
import com.stella.rememberall.item.ItemPurchasedByUser;
import com.stella.rememberall.tripLog.TripLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_tbl")
@Entity
public class User extends BaseTimeEntity implements UserDetails {

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 300)
    private String password;

    @Column(name="kakao_id", unique = true)
    private Long kakaoId;

    @Column(length = 300, nullable = false)
    private String name;

    @Column(name="term_agree")
    private Boolean termAgree;

    @Column(name="alarm_agree")
    private Boolean alarmAgree;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @PrimaryKeyJoinColumn
//    @JoinColumn(name="dongdong_id")
    private Dongdong dongdong;

    @OneToMany(mappedBy = "user")
    private List<TripLog> tripLogList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ItemPurchasedByUser> itemPurchasedByUserList = new ArrayList<>();

    @Builder
    public User(AuthType authType, String email, String password, String name){
        this.authType = authType;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // UserDetails //
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // update
    public User updateName(String name){
        this.name = name;
        return this;
    }

    public User updateAlarmAgree(Boolean alarmAgree){
        this.alarmAgree = alarmAgree;
        return this;
    }

    public User updateTermAgree(Boolean termAgree){
        this.termAgree = termAgree;
        return this;
    }

    public User updatePassword(String password){
        this.password = password;
        return this;
    }



}
