package com.goormpj.decimal.board.mapper;

import com.goormpj.decimal.board.dto.RecruitApplicationDTO;
import com.goormpj.decimal.board.entity.RecruitInfo;
import com.goormpj.decimal.board.entity.RecruitPost;
import com.goormpj.decimal.board.entity.State;

import java.util.List;
import java.util.stream.Collectors;

public class RecruitApplicationMapper {


    public static List<RecruitApplicationDTO> toDto(List<RecruitInfo> recruitInfos) {
        return recruitInfos.stream().map(recruitInfo -> {
            RecruitPost recruitPost = recruitInfo.getRecruitPost();
            RecruitApplicationDTO dto = new RecruitApplicationDTO();
            dto.setId(recruitPost.getId());
            dto.setTitle(recruitPost.getTitle());
            dto.setContent(recruitPost.getContent());
            dto.setApplied(recruitPost.getApplied());
            dto.setRecruited(recruitPost.getRecruited());
            dto.setState(recruitPost.getState());
            dto.setTarget(recruitPost.getTarget());
            dto.setCreatedAt(recruitPost.getCreatedAt());
            dto.setUpdatedAt(recruitPost.getUpdatedAt());
            dto.setMyState(recruitInfo.getState());
            return dto;
        }).collect(Collectors.toList());
    }

    public static RecruitApplicationDTO entityToDto(RecruitPost recruitPost) {
        RecruitApplicationDTO dto = new RecruitApplicationDTO();
        dto.setId(recruitPost.getId());
        dto.setTitle(recruitPost.getTitle());
        dto.setContent(recruitPost.getContent());
        dto.setApplied(recruitPost.getApplied());
        dto.setRecruited(recruitPost.getRecruited());
        dto.setState(recruitPost.getState());
        dto.setTarget(recruitPost.getTarget());
        dto.setCreatedAt(recruitPost.getCreatedAt());
        dto.setUpdatedAt(recruitPost.getUpdatedAt());
        return dto;
    }

}
