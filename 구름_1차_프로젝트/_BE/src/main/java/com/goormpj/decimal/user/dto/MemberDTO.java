package com.goormpj.decimal.user.dto;

import com.goormpj.decimal.user.domain.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private String email;

    private String nickname;

    private String authority;

}
