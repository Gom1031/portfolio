package com.goormpj.decimal.user.mapper;

import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.dto.CustomUserDetails;

public class CustomUserDetailsMapper {
    public static Member detailsToMember(CustomUserDetails customUserDetails){
        return Member.builder()
                .id(Long.valueOf(customUserDetails.getUsername()))
                .password(customUserDetails.getPassword())
                .build();
    }
}
