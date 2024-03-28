package com.goormpj.decimal.board.mapper;

import com.goormpj.decimal.board.dto.RecruitPostRequestDTO;
import com.goormpj.decimal.board.dto.RecruitPostResponseDTO;
import com.goormpj.decimal.board.entity.RecruitPost;

public class RecruitPostMapper {

    public static RecruitPost requestDtoToEntity(RecruitPostRequestDTO requestDTO) {
        RecruitPost recruitPost = new RecruitPost();
        recruitPost.setTitle(requestDTO.getTitle());
        recruitPost.setContent(requestDTO.getContent());
        recruitPost.setRecruited(requestDTO.getRecruited());
        recruitPost.setTarget(requestDTO.getTarget());

        return recruitPost;
    }

    public static RecruitPostResponseDTO entityToResponseDto(RecruitPost recruitPost) {
        RecruitPostResponseDTO dto = new RecruitPostResponseDTO();
        dto.setId(recruitPost.getId());
        dto.setTitle((recruitPost.getTitle()));
        dto.setContent(recruitPost.getContent());
        dto.setRecruited(recruitPost.getRecruited());
        dto.setState(recruitPost.getState());
        dto.setTarget(recruitPost.getTarget());
        dto.setCreatedAt(recruitPost.getCreatedAt());
        dto.setUpdatedAt(recruitPost.getUpdatedAt());
        dto.setIsDeleted(recruitPost.getIsDeleted());
        dto.setApplied(recruitPost.getApplied());
        return dto;


    }
}