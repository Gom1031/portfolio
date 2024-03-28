package com.goormpj.decimal.user.controller;

import com.goormpj.decimal.user.dto.MailRequestDTO;
import com.goormpj.decimal.user.dto.VerificationEmailRequest;
import com.goormpj.decimal.user.service.MailService;
import com.goormpj.decimal.user.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verify-email")
@RequiredArgsConstructor
@Slf4j
public class MailController {

    private final MailService mailService;

    private final MemberService memberService;

    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationEmail(@Valid @RequestBody VerificationEmailRequest verificationEmailRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        String errorMessage = validateVerificationEmailRequest(verificationEmailRequest);
        if (errorMessage != null) {
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            mailService.sendMail(verificationEmailRequest);
            return ResponseEntity.ok("인증 메일 전송 완료");
        } catch (Exception e) {
            log.error("Failed to send verification email", e);
            return ResponseEntity.badRequest().body("error: 인증 메일 전송 실패");
        }
    }

    private ResponseEntity<String> handleValidationErrors(BindingResult bindingResult) {
        StringBuilder errors = new StringBuilder();
        bindingResult.getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.append(errorMessage).append("\n");
        });
        return ResponseEntity.badRequest().body(errors.toString());
    }

    private String validateVerificationEmailRequest(VerificationEmailRequest verificationEmailRequest) {
        if (verificationEmailRequest.getType().equals("email")) {
            if (memberService.existsByEmail(verificationEmailRequest.getEmail())) {
                return "이미 가입된 이메일 주소입니다.";
            }
        } else if (verificationEmailRequest.getType().equals("password")) {
            if (!memberService.existsByEmail(verificationEmailRequest.getEmail())) {
                return "사용자를 찾을 수 없습니다.";
            }
        }
        return null;
    }

    @PostMapping("/valid")
    public ResponseEntity<String> verifyEmail(@RequestBody MailRequestDTO mailRequestDTO) {
        Boolean isValid = mailService.verifyEmail(mailRequestDTO.getToken());
        if (isValid) {
            return ResponseEntity.ok("이메일 인증 완료 - " + mailRequestDTO.getType());
        } else {
            return ResponseEntity.badRequest().body("메일 인증 토큰이 유효하지 않습니다.");
        }
    }


}
