package com.goormpj.decimal.jwt;

import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.dto.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청에서 모든 헤더를 로깅
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            Collections.list(request.getHeaders(headerName)).forEach(headerValue -> {
                log.info("Header '{}': {}", headerName, headerValue);
            });
        });

        // 헤더에서 access_token 꺼내기
        String accessToken = request.getHeader("access_token");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            log.info("No access_token found in request headers.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
            jwtProvider.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            log.error("Access token expired: " + e.getMessage(), e);
            PrintWriter writer = response.getWriter();
            writer.print("access_token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 error
            return;
        } catch (Exception e) {
            log.error("Error processing access_token: " + e.getMessage(), e);
            PrintWriter writer = response.getWriter();
            writer.print("Error in access_token processing");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 error
            return;
        }

        try {
            // 토큰이 access_token 인지 확인 (발급시 페이로드에 명시)
            String category = jwtProvider.getCategory(accessToken);
            if (!category.equals("access_token")) {
                log.warn("Invalid access_token category: expected 'access_token' but found '" + category + "'");
                PrintWriter writer = response.getWriter();
                writer.print("invalid access_token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 error
                return;
            }
        } catch (Exception e) {
            log.error("Error validating access_token category: " + e.getMessage(), e);
            PrintWriter writer = response.getWriter();
            writer.print("Error validating access_token");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 error
            return;
        }

        // Token 사용자 정보 추출 및 인증 처리
        try {
            String memberId = jwtProvider.getMemberId(accessToken);
            Member member = new Member(Long.parseLong(memberId));
            log.info("JWTFilter class - member.getId() {}", member.getId());

            CustomUserDetails customUserDetails = new CustomUserDetails(member);
            log.info("JWTFilter class - customUserDetails.getUsername() {}", customUserDetails.getUsername());
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (NumberFormatException e) {
            log.error("Invalid memberId format in access_token: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 error
            return;
        } catch (Exception e) {
            log.error("Error setting authentication in security context: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 error
            return;
        }

        filterChain.doFilter(request, response);
    }

}
