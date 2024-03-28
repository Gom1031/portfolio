package com.goormpj.decimal.ide.mapper;

import com.goormpj.decimal.ide.dto.CodeChangeDto;
import com.goormpj.decimal.ide.domain.CodeChangeModel;

public class CodeChangeMapper {
    public static CodeChangeModel dtoToEntity(CodeChangeDto dto) {
        CodeChangeModel entity = new CodeChangeModel();
        entity.setUserId(dto.getUserId());
        entity.setFileId(dto.getFileId());
        entity.setSessionId(dto.getSessionId());
        entity.setContent(dto.getContent());
        entity.setTimestamp(dto.getTimestamp());
        return entity;
    }

    public static CodeChangeDto entityToDto(CodeChangeModel entity) {
        CodeChangeDto dto = new CodeChangeDto();
        dto.setUserId(entity.getUserId());
        dto.setFileId(entity.getFileId());
        dto.setSessionId(entity.getSessionId());
        dto.setContent(entity.getContent());
        dto.setTimestamp(entity.getTimestamp());
        return dto;
    }
}
