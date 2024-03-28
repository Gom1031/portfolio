package com.goormpj.decimal.user.mapper;

import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.dto.LoginResponseDTO;

public class MemberMapper {

    public static LoginResponseDTO entityToDto(Member member){
        return LoginResponseDTO.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileFilename(member.getProfileFilename())
                .profileFilepath(member.getProfileFilepath())
                .build();
    }

}
