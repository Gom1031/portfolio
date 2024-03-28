package com.goormpj.decimal.user.oauth2.dto;

import com.goormpj.decimal.user.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final MemberDTO memberDTO;

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberDTO.getAuthority();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return memberDTO.getNickname();
    }

    public String getUsername() {
        return memberDTO.getEmail();
    }
}
