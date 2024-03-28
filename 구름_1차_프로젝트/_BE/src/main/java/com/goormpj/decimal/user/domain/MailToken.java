package com.goormpj.decimal.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MailToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;

    private String email;
    private LocalDateTime expiryDate;

    @Builder
    private MailToken(String token, String email, LocalDateTime expiryDate) {
        this.token = token;
        this.email = email;
        this.expiryDate = expiryDate;
    }

    public static MailToken createMailToken(String userEmail) {
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(5L);
        String token = generateToken(); // 토큰 생성
        return MailToken.builder()
                .token(token)
                .email(userEmail)
                .expiryDate(expiryDate)
                .build();
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
