package com.goormpj.decimal.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {

    @Email
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 15, message = "닉네임은 2~15자여야 합니다.")
    @Pattern(regexp = "^[^\\s]*$", message = "닉네임에 공백은 포함될 수 없습니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 영문, 숫자, 특수기호를 적어도 1개 이상씩 포함하여 8~15자여야 합니다."
    )
    private String password;

}
