package com.goormpj.decimal.user.oauth2.service;

import com.goormpj.decimal.user.domain.Authority;
import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.oauth2.dto.OAuth2Response;
import com.goormpj.decimal.user.oauth2.dto.CustomOAuth2User;
import com.goormpj.decimal.user.dto.MemberDTO;
import com.goormpj.decimal.user.oauth2.dto.NaverResponse;
import com.goormpj.decimal.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        Member existMember = memberRepository.findByEmail(oAuth2Response.getEmail());

        if(existMember == null){
            Member member = Member.builder()
                    .email(oAuth2Response.getEmail())
                    .nickname(oAuth2Response.getName() + (int)(Math.random()*10000000))
                    .password("")
                    .authority(Authority.ROLE_USER)
                    .build();

            memberRepository.save(member);
        }

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail(oAuth2Response.getEmail());
        memberDTO.setNickname(oAuth2Response.getName());
        memberDTO.setAuthority("ROLE_USER");


        return new CustomOAuth2User(memberDTO);
    }
}