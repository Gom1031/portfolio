package com.goormpj.decimal.board.mapper;

import com.goormpj.decimal.board.dto.RecruitInfoDTO;
import com.goormpj.decimal.board.entity.RecruitInfo;
import com.goormpj.decimal.user.domain.Member;

public class RecruitInfoMapper {

    public static RecruitInfoDTO toDto(RecruitInfo recruitInfo) {
        if (recruitInfo == null) {
            return null;
        }

        RecruitInfoDTO dto = new RecruitInfoDTO();
        dto.setMotivation(recruitInfo.getMotivation());
        dto.setId(recruitInfo.getId());
        dto.setUserNickname(recruitInfo.getMember().getNickname());
        dto.setCreatedAt(recruitInfo.getCreatedAt());
        dto.setState(recruitInfo.getState());
        return dto;
    }

    public static RecruitInfo toEntity(RecruitInfoDTO dto) {
        if (dto == null) {
            return null;
        }

        RecruitInfo recruitInfo = new RecruitInfo();
        recruitInfo.setMotivation(dto.getMotivation());
        recruitInfo.setId(dto.getId());
        return recruitInfo;
    }
}
