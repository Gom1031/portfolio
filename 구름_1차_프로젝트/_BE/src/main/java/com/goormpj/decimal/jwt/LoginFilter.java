package com.goormpj.decimal.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goormpj.decimal.user.domain.RefreshToken;
import com.goormpj.decimal.user.dto.CustomUserDetails;
import com.goormpj.decimal.user.dto.LoginRequestDTO;
import com.goormpj.decimal.user.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTProvider jwtProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginRequestDTO = objectMapper.readValue(messageBody, LoginRequestDTO.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();

        //스프링 시큐리티에서 email,password 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        //검증을 위한 AuthenticationManager로 token 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행되는 메서드 (여기서 jwt 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String memberId = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //create JWT token
        String access_token = jwtProvider.createJwt("access_token", memberId, role, 600000L);
        String refresh_token = jwtProvider.createJwt("refresh_token", memberId, role, 86400000L);

        addRefreshToken(memberId, refresh_token, 86400000L);

        response.setHeader("access_token", access_token);
        response.addCookie(createCookie("refresh_token", refresh_token));
        response.setStatus(HttpStatus.OK.value());
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

    //로그인 실패시 실행되는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        String errorMessage = "로그인 실패 : " + failed.getMessage();
        log.info("login fail / errorMessage = {}", failed.getMessage());
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(errorMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true); //https
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

}
