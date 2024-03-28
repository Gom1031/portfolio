package com.goormpj.decimal.user.service;

import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.dto.CustomUserDetails;
import com.goormpj.decimal.user.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member memberData = memberRepository.findByEmail(username);
        //Optional<Member> findByEmailOrOauthId(String emailOrOauthId);

        if (memberData != null) {
            return new CustomUserDetails(memberData);
        }
        return (UserDetails) new UsernameNotFoundException("userId not found: " + username);
    }
}
