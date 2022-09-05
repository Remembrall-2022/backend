package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    private Long point;

    @Setter
    private Long exp;

    public Dongdong (User user) {
        this.user = user;
        this.point = 0L;
        this.exp = 0L;
    }
}
