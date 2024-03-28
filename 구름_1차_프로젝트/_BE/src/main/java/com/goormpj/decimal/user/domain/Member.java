package com.goormpj.decimal.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String email;

    private String nickname;

    private String password;

    private String profileFilename;

    private String profileFilepath;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String email, String nickname, String password, Authority authority) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.authority = authority;
    }

    public Member(Long id) {
        this.id = id;
        this.password = "temppassword1!";
        this.authority = Authority.ROLE_USER;
    }


    //@Builder
    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    //@Builder
    public void updatePassword(String password){
        this.password = password;
    }



}
