package com.goormpj.decimal.chat.dto;

import com.goormpj.decimal.chat.model.VideoChatModel;
import com.goormpj.decimal.user.domain.Member;
import lombok.Data;

import java.util.Map;

@Data
public class VideoChatDto {
    private Map<String, Object> properties;

    private Member member;
    private Boolean isPublisher;
}
