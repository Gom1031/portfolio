package com.goormpj.decimal.board.dto;

import com.goormpj.decimal.board.entity.State;
import com.goormpj.decimal.user.domain.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecruitInfoDTO {
    private Long id;
    private String motivation;
    private String userNickname;
    private State state;
    private LocalDateTime createdAt;
}
