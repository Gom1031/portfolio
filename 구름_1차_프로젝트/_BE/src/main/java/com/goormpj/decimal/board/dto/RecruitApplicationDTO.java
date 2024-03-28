package com.goormpj.decimal.board.dto;

import com.goormpj.decimal.board.entity.State;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecruitApplicationDTO {
    private Long id;                // 부모 게시글 Id
    private String title;           // 게시글 제목
    private String content;         // 게시글 내용
    private Integer applied; // 현재까지 지원한 인원
    private Integer recruited; // 총 모집 인원
    private Boolean state;           // 게시글 모집 상태
    private String target;          // 게시글 모집 대상
    private LocalDateTime createdAt;        // 생성 시각
    private LocalDateTime updatedAt;        // 업데이트 시각
    private State myState; // 내 지원 상태
}
