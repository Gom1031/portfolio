package com.goormpj.decimal.ide.mapper;

import com.goormpj.decimal.ide.domain.Study;
import com.goormpj.decimal.ide.dto.StudyRequestDTO;
import com.goormpj.decimal.ide.dto.StudyResponseDTO;

public class StudyMapper {
    public static Study requestDtoToEntity(StudyRequestDTO requestDTO) {
        Study study = new Study();
        study.setName(requestDTO.getName());
        study.setMemberCount(requestDTO.getMemberCount());

        return study;
    }

    public static StudyResponseDTO entityToResponseDto(Study study) {
        StudyResponseDTO dto = new StudyResponseDTO();
        dto.setId(study.getId());
        dto.setIsLeader(study.getIsLeader());
        dto.setName(study.getName());
        dto.setMemberCount(study.getMemberCount());

        return dto;
    }
}
