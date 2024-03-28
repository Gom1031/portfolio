package com.goormpj.decimal.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;

    private String refreshToken;

    private Date expirationDate;

    @Builder
    public RefreshToken(String memberId, String refreshToken, Date expirationDate) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
        this.expirationDate = expirationDate;
    }

}
