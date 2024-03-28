package com.goormpj.decimal.board.service;

import com.goormpj.decimal.board.dto.RecruitPostRequestDTO;
import com.goormpj.decimal.board.dto.RecruitPostResponseDTO;
import com.goormpj.decimal.board.entity.RecruitPost;
import com.goormpj.decimal.user.dto.CustomUserDetails;

import java.util.List;
import java.util.Optional;

public interface RecruitPostService {
    List<RecruitPostResponseDTO> findAllNotDeleted();
    List<RecruitPostResponseDTO> getMyRecruitPosts(CustomUserDetails customUserDetails);
    Optional<RecruitPost> findByIdNotDeleted(Long id);

    List<RecruitPostResponseDTO> findByIdNotDeleted(Long id, CustomUserDetails customUserDetails);
    RecruitPost createRecruitPost(RecruitPostRequestDTO requestDTO, CustomUserDetails customUserDetails);
    RecruitPost updateRecruitPost(Long id, RecruitPost recruitPost);
    void softDelete(Long id);
    void updateRecruitmentState(Long postId, Boolean newState); // 메서드 추가

}