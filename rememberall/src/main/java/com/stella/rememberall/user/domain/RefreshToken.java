package com.stella.rememberall.user.domain;

import com.stella.rememberall.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userPk;

    @Column(name = "refresh_token_value", nullable = false)
    private String refreshTokenValue;

    public RefreshToken updateRefreshTokenValue(String refreshTokenValue) {
        this.refreshTokenValue = refreshTokenValue;
        return this;
    }

    @Builder
    public RefreshToken(Long key, String refreshTokenValue) {
        this.userPk = key;
        this.refreshTokenValue = refreshTokenValue;
    }
}