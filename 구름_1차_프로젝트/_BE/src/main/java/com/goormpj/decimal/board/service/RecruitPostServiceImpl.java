package com.goormpj.decimal.board.service;

import com.goormpj.decimal.board.dto.RecruitPostRequestDTO;
import com.goormpj.decimal.board.dto.RecruitPostResponseDTO;
import com.goormpj.decimal.board.entity.RecruitInfo;
import com.goormpj.decimal.board.entity.RecruitPost;
import com.goormpj.decimal.board.mapper.RecruitInfoMapper;
import com.goormpj.decimal.board.mapper.RecruitPostMapper;
import com.goormpj.decimal.board.repository.RecruitPostRepository;
import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.dto.CustomUserDetails;
import com.goormpj.decimal.user.mapper.CustomUserDetailsMapper;
import com.goormpj.decimal.user.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecruitPostServiceImpl implements RecruitPostService {
    private final RecruitPostRepository recruitPostRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public RecruitPostServiceImpl(RecruitPostRepository recruitPostRepository,
                                  MemberRepository memberRepository) {
        this.recruitPostRepository = recruitPostRepository;
        this.memberRepository = memberRepository;
    }


    @Override
    public List<RecruitPostResponseDTO> findAllNotDeleted() {
        return recruitPostRepository.findByIsDeletedFalse().stream()
                .map(RecruitPostMapper::entityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RecruitPost> findByIdNotDeleted(Long id) {
        return recruitPostRepository.findByIdAndIsDeletedFalse(id);
    }

    // ID로 특정 모집 게시글 조회
    @Override
    public List<RecruitPostResponseDTO> findByIdNotDeleted(Long id, CustomUserDetails customUserDetails) {
        return recruitPostRepository.findByIdAndIsDeletedFalse(id)
                .map(recruitPost -> {
                    RecruitPostResponseDTO responseDTO = new RecruitPostResponseDTO();
                    responseDTO.setId(recruitPost.getId());
                    responseDTO.setTitle(recruitPost.getTitle());
                    responseDTO.setContent(recruitPost.getContent());
                    responseDTO.setApplied(recruitPost.getApplied());
                    responseDTO.setRecruited(recruitPost.getRecruited());
                    responseDTO.setState(recruitPost.getState());
                    responseDTO.setTarget(recruitPost.getTarget());
                    responseDTO.setCreatedAt(recruitPost.getCreatedAt());
                    responseDTO.setUpdatedAt(recruitPost.getUpdatedAt());
                    responseDTO.setIsDeleted(recruitPost.getIsDeleted());

                    boolean isWriter = recruitPost.getWriter().getId().equals(Long.valueOf(customUserDetails.getUsername()));
                    responseDTO.setIsWriter(isWriter);

                    return responseDTO;
                })
                .stream()
                .collect(Collectors.toList());
    }



    // 새 모집 게시글 생성
    @Override
    public RecruitPost createRecruitPost(RecruitPostRequestDTO requestDTO, CustomUserDetails customUserDetails) {
        RecruitPost responsePost = RecruitPostMapper.requestDtoToEntity(requestDTO);
        Member userDetail = CustomUserDetailsMapper.detailsToMember(customUserDetails);

        responsePost.setWriter(userDetail);
        return recruitPostRepository.save(responsePost);
    }

    // 로그인 한 사용자가 작성한 모든 모집 게시글 조회
    @Override
    public List<RecruitPostResponseDTO> getMyRecruitPosts(CustomUserDetails customUserDetails) {
        Long userId = Long.valueOf(customUserDetails.getUsername());
        List<RecruitPost> recruitInfos = recruitPostRepository.findByWriterIdAndIsDeletedFalse(userId);

        return recruitInfos.stream()
                .map(RecruitPostMapper::entityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public RecruitPost updateRecruitPost(Long id, RecruitPost updateDetails) {
        RecruitPost recruitPost = recruitPostRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("No such post found"));

        recruitPost.setTitle(updateDetails.getTitle());
        recruitPost.setContent(updateDetails.getContent());
        recruitPost.setRecruited(updateDetails.getRecruited());
        recruitPost.setState(updateDetails.getState());
        recruitPost.setTarget(updateDetails.getTarget());

        return recruitPostRepository.save(recruitPost);
    }


    @Override
    public void softDelete(Long id) {
        RecruitPost recruitPost = recruitPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with Id: " + id));
        recruitPost.setIsDeleted(true);
        recruitPostRepository.save(recruitPost);
    }

    //모집 상태 업데이트 메소드
    @Override
    public void updateRecruitmentState(Long postId, Boolean newState) {
        RecruitPost post = recruitPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with Id: " + postId));
        post.setState(newState);
        recruitPostRepository.save(post);
    }
}