package com.goormpj.decimal.user.service;

import com.goormpj.decimal.jwt.JWTProvider;
import com.goormpj.decimal.user.domain.RefreshToken;
import com.goormpj.decimal.user.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReissueService {

    private final JWTProvider jwtProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    public String[] renewRefreshToken(String refreshToken) {

        if (refreshToken == null) {
            log.info("refresh_token null");
            return new String[]{};
        }

        //expired check
        try {
            jwtProvider.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            log.info("refresh_token expired");
            return new String[]{};
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtProvider.getCategory(refreshToken);

        if (!category.equals("refresh_token")){
            log.info("invalid refresh_token");
            return new String[]{};
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if (!isExist) {
            log.info("invalid refresh_token");
            return new String[]{};
        }

        String memberId = jwtProvider.getMemberId(refreshToken);
        String role = jwtProvider.getRole(refreshToken);

        String newAccessToken = jwtProvider.createJwt("access_token", memberId, role, 600000L);
        String newRefreshToken = jwtProvider.createJwt("refresh_token", memberId, role, 86400000L);

        //DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
        addRefreshToken(memberId, newRefreshToken, 86400000L);

        String[] refreshTokenArr = new String[2];
        refreshTokenArr[0] = newAccessToken;
        refreshTokenArr[1] = newRefreshToken;

        return refreshTokenArr;
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
}
