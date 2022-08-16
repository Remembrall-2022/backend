package com.stella.rememberall.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmailAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column
    private String authCode;

    @Column
    private LocalDateTime authValidTime;

    @Column
    private Boolean authed;

    @Builder
    public EmailAuth(String email, String authCode, LocalDateTime authValidTime, Boolean authed){
        this.email = email;
        this.authCode = authCode;
        this.authValidTime = authValidTime;
        this.authed = authed;
    }

    public EmailAuth updateAuthed(Boolean authed){
        this.authed = authed;
        return this;
    }

    public EmailAuth updateAuthCode(String authCode){
        this.authCode = authCode;
        return this;
    }

    public EmailAuth updateAuthValidTime(LocalDateTime authValidTime){
        this.authValidTime = authValidTime;
        return this;
    }
}
