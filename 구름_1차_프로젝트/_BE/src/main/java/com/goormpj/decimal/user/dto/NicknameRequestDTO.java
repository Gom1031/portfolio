package com.goormpj.decimal.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NicknameRequestDTO {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 15, message = "닉네임은 2~15자여야 합니다.")
    @Pattern(regexp = "^[^\\s]*$", message = "닉네임에 공백은 포함될 수 없습니다.")
    private String nickname;

}
