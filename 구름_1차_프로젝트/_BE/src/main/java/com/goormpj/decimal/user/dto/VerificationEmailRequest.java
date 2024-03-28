package com.goormpj.decimal.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationEmailRequest {

    @NotNull
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @Builder.Default
    private Boolean resend = false;

    private String type;

}
