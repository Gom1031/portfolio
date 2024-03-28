package com.goormpj.decimal.user.scheduler;

import com.goormpj.decimal.user.domain.MailToken;
import com.goormpj.decimal.user.domain.RefreshToken;
import com.goormpj.decimal.user.repository.MailTokenRepository;
import com.goormpj.decimal.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    private final MailTokenRepository mailTokenRepository;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredRefreshTokens() {
        List<RefreshToken> expiredTokens = refreshTokenRepository.findByExpirationDateBefore(new Date());
        refreshTokenRepository.deleteAll(expiredTokens);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredMailTokens() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<MailToken> expiredTokens = mailTokenRepository.findByExpiryDateBefore(currentDateTime);
        mailTokenRepository.deleteAll(expiredTokens);
    }

}
