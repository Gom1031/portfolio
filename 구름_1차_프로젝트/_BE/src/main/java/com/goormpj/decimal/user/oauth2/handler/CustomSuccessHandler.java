package com.goormpj.decimal.user.oauth2.handler;

import com.goormpj.decimal.jwt.JWTProvider;
import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.domain.RefreshToken;
import com.goormpj.decimal.user.oauth2.dto.CustomOAuth2User;
import com.goormpj.decimal.user.repository.MemberRepository;
import com.goormpj.decimal.user.repository.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTProvider jwtProvider;

    private final MemberRepository memberRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("onAuthenticationSuccess 호출");
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        log.info("onAuthenticationSuccess - username {}", username);

        Member member = memberRepository.findByEmail(username);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access_token = jwtProvider.createJwt("access_token", String.valueOf(member.getId()), role, 600000L);
        String refresh_token = jwtProvider.createJwt("refresh_token", String.valueOf(member.getId()), role, 86400000L);

        addRefreshToken(String.valueOf(member.getId()), refresh_token, 86400000L);

        response.setHeader("access_token", access_token);
        response.addCookie(createCookie("refresh_token", refresh_token));
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("https://groomcosmos.site/socialLogin");

    }

    private void addRefreshToken(String memberId, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshToken refreshToken = RefreshToken.builder()
                .memberId(memberId)
                .refreshToken(refresh)
                .expirationDate(date)
                .build();
        refreshTokenRepository.save(refreshToken);

    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60*60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
