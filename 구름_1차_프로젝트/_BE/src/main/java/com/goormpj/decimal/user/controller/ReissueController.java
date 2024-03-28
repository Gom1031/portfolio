package com.goormpj.decimal.user.controller;

import com.goormpj.decimal.user.service.ReissueService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh_token from cookies
        String refreshToken = getRefreshToken(request);

        String[] renewTokens = reissueService.renewRefreshToken(refreshToken);
        if(renewTokens.length == 0){
            return new ResponseEntity<>("refresh_token is null OR invalid refresh_token", HttpStatus.BAD_REQUEST);
        }

        String newAccessToken = renewTokens[0];
        String newRefreshToken = renewTokens[1];

        response.setHeader("access_token", newAccessToken);
        response.addCookie(createCookie("refresh_token", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
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
