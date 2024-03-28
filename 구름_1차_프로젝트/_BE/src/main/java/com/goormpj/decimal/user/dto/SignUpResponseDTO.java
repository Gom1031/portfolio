package com.goormpj.decimal.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDTO {

    private String email;

    private String nickname;

    private String password;

}
