package com.goormpj.decimal.chat.mapper;

import com.goormpj.decimal.chat.dto.ChatMessageDto;
import com.goormpj.decimal.chat.model.ChatMessageModel;
import com.goormpj.decimal.chat.model.VideoChatModel;
import org.modelmapper.ModelMapper;

public class ChatMessageMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ChatMessageModel toModel(ChatMessageDto dto) {
        ChatMessageModel chatMessageModel = modelMapper.map(dto, ChatMessageModel.class);
        return chatMessageModel;
    }
    public static ChatMessageDto toDto(ChatMessageModel model) {
        ChatMessageDto chatMessageDto = modelMapper.map(model, ChatMessageDto.class);
        return chatMessageDto;
    }
}