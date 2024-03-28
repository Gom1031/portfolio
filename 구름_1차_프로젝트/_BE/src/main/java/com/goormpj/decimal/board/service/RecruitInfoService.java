package com.goormpj.decimal.board.service;

import com.goormpj.decimal.board.dto.RecruitApplicationDTO;
import com.goormpj.decimal.board.dto.RecruitInfoDTO;
import com.goormpj.decimal.board.dto.RecruitPostResponseDTO;
import com.goormpj.decimal.user.dto.CustomUserDetails;

import java.util.List;

public interface RecruitInfoService {
    RecruitInfoDTO createRecruitInfo(RecruitInfoDTO recruitInfoDTO, Long parentPostId, CustomUserDetails customUserDetails);
    List<RecruitInfoDTO> getRecruitInfosByParentPostId(Long parentPostId);
    List<RecruitInfoDTO> getMyRecruitInfos(CustomUserDetails customUserDetails);
    List<RecruitApplicationDTO> getMyRecruitPost(CustomUserDetails customUserDetails);

    void deleteRecruitInfo(Long id);
    RecruitInfoDTO updateRecruitInfo(Long id, RecruitInfoDTO recruitInfoDTO);
    void respondToRecruit(Long id, boolean accept);
}