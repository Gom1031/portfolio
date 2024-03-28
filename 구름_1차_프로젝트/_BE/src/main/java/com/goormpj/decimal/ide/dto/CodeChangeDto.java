package com.goormpj.decimal.ide.dto;

import lombok.Data;

@Data
public class CodeChangeDto {
    private String userId;
    private Long fileId;
    private String sessionId;
    private String content;
    private long timestamp;
}
