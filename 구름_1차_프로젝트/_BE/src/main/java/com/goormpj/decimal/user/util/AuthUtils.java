package com.goormpj.decimal.user.util;

import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.dto.CustomUserDetails;
import com.goormpj.decimal.user.dto.LoginResponseDTO;
import com.goormpj.decimal.user.mapper.MemberMapper;
import com.goormpj.decimal.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final MemberService memberService;

    public Optional<Member> getLoggedInMember(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            return Optional.empty();
        }

        String id = customUserDetails.getUsername();
        return Optional.ofNullable(memberService.findById(Long.parseLong(id)));
    }

}
