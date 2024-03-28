package com.goormpj.decimal.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitPostRequestDTO {
    private String title;           // 작성 게시글 제목
    private String content;         // 작성 게시글 내용
    private Integer recruited;      // 모집 인원
    private String target;          // 모집 대상
}
