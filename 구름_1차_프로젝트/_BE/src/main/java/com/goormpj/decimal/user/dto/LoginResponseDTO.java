package com.goormpj.decimal.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private Long id;

    private String email;

    private String nickname;

    private String profileFilename;

    private String profileFilepath;

}
