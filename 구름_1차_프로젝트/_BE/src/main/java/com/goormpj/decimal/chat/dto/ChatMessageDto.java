package com.goormpj.decimal.chat.dto;

import lombok.Data;

@Data
public class ChatMessageDto {
    private Long id;
    private String sessionId;
    private String senderId;
    private String message;
}
