package com.goormpj.decimal.user.service;

import com.goormpj.decimal.user.domain.MailToken;
import com.goormpj.decimal.user.dto.VerificationEmailRequest;
import com.goormpj.decimal.user.repository.MailTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MailService {

    private final JavaMailSender javaMailSender;

    private final MailTokenRepository mailTokenRepository;

    @Value("${spring.verificationUrl}")
    private String verificationUrl;

    public void sendMail(VerificationEmailRequest verificationEmailRequest) throws IOException {
        MailToken mailToken = generateMailToken(verificationEmailRequest.getEmail());
        String link = generateVerificationMailLink(mailToken.getToken(), verificationEmailRequest);

        String htmlFileName, subject;
        if(verificationEmailRequest.getType().equals("email")){
            htmlFileName = "verification-email.html";
            subject = "[COSMO'S] 가입 인증 메일입니다.";
        }else{
            htmlFileName = "verification-email-forpass.html";
            subject = "[COSMO'S] 비밀번호 재설정 인증 메일입니다.";
        }
        String htmlContent = readHtmlContent(htmlFileName);
        htmlContent = htmlContent.replace("{{verificationLink}}", link);

        String finalHtmlContent = htmlContent;
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(verificationEmailRequest.getEmail());
            helper.setSubject(subject);

            helper.setText(finalHtmlContent, true);
        };

        javaMailSender.send(preparator);

        MailToken token = mailTokenRepository.findByEmail(verificationEmailRequest.getEmail());
        if(verificationEmailRequest.getResend() || token != null){
            mailTokenRepository.delete(token);
        }

        mailTokenRepository.save(mailToken);
    }

    public MailToken generateMailToken(String userEmail) {
        return MailToken.createMailToken(userEmail);
    }

    public String generateVerificationMailLink(String token, VerificationEmailRequest verificationEmailRequest) {
        // 생성된 토큰을 이용하여 인증 링크 생성
        String verificationLink = verificationUrl + "/setPassword?token=" + token + "&type=" + verificationEmailRequest.getType() + "&email=" + verificationEmailRequest.getEmail();
        return verificationLink;
    }

    private String readHtmlContent(String htmlFileName) throws IOException {
        // resources/templates 폴더에 있는 HTML 파일을 읽어서 문자열로 반환합니다.
        ClassPathResource resource = new ClassPathResource("templates/" + htmlFileName);
        byte[] htmlByteArray = resource.getInputStream().readAllBytes();
        return new String(htmlByteArray, StandardCharsets.UTF_8);
    }

    public Boolean verifyEmail(String token) {
        // 토큰 비어있는지 확인
        if (token == null || token.isEmpty()) {
            return false;
        }

        // 토큰 만료 확인
        MailToken expiryDate = mailTokenRepository.findExpriyDateByToken(token);
        if (expiryDate == null || expiryDate.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        // 만료되지 않았을 경우 true 반환
        return true;

    }


}
