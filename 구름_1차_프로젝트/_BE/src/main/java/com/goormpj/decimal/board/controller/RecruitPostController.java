package com.goormpj.decimal.board.controller;

import com.goormpj.decimal.board.dto.RecruitInfoDTO;
import com.goormpj.decimal.board.dto.RecruitPostDTO;
import com.goormpj.decimal.board.dto.RecruitPostRequestDTO;
import com.goormpj.decimal.board.dto.RecruitPostResponseDTO;
import com.goormpj.decimal.board.service.RecruitPostService;
import com.goormpj.decimal.board.mapper.RecruitPostMapper;
import com.goormpj.decimal.board.entity.RecruitPost;
import com.goormpj.decimal.user.dto.CustomUserDetails;
import com.goormpj.decimal.user.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication; // 추가
import org.springframework.security.core.context.SecurityContextHolder;
import com.goormpj.decimal.user.service.MemberService;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recruit")
public class RecruitPostController {
    private final RecruitPostService recruitPostService;
    private final MemberService memberService;

    @Autowired
    public RecruitPostController(RecruitPostService recruitPostService, MemberService memberService) {
        this.recruitPostService = recruitPostService;
        this.memberService = memberService;
    }

    // 모든 모집 게시글 조회
    @GetMapping
    public ResponseEntity<List<RecruitPostResponseDTO>> getAllRecruitPosts() {
        List<RecruitPostResponseDTO> dtos = recruitPostService.findAllNotDeleted();
        return ResponseEntity.ok(dtos);
    }


    // ID로 특정 모집 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<List<RecruitPostResponseDTO>> getRecruitPostById(@PathVariable Long id,
                                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<RecruitPostResponseDTO> recruitPosts = recruitPostService.findByIdNotDeleted(id, customUserDetails);

        return ResponseEntity.ok(recruitPosts);
    }

    // 로그인 한 사용자가 작성한 모든 모집 게시글 조회
    @GetMapping("/myPost")
    public ResponseEntity<List<RecruitPostResponseDTO>> getMyRecruitInfos(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<RecruitPostResponseDTO> myRecruitPostDTOs = recruitPostService.getMyRecruitPosts(customUserDetails);
        return ResponseEntity.ok(myRecruitPostDTOs);
    }


    // 새 모집 게시글 생성
    @CrossOrigin
    @PostMapping
    public ResponseEntity<RecruitPostResponseDTO> createRecruitPost(@RequestBody RecruitPostRequestDTO requestDTO,
                                                                    @AuthenticationPrincipal(errorOnInvalidType=true) CustomUserDetails customUserDetails) {
        RecruitPost savedPost = recruitPostService.createRecruitPost(requestDTO, customUserDetails);
        RecruitPostResponseDTO responseDTO = RecruitPostMapper.entityToResponseDto(savedPost);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    // 특정 모집 게시글을 업데이트하는 메서드
    @PutMapping("/{id}")
    public ResponseEntity<RecruitPostResponseDTO> updateRecruitPost(@PathVariable Long id,
                                                                    @RequestBody RecruitPostRequestDTO requestDTO,
                                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = Long.valueOf(customUserDetails.getUsername());

        Optional<RecruitPost> recruitPostOpt = recruitPostService.findByIdNotDeleted(id);
        if (recruitPostOpt.isEmpty() || !recruitPostOpt.get().getWriter().getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        RecruitPost updatedPost = recruitPostService.updateRecruitPost(id, RecruitPostMapper.requestDtoToEntity(requestDTO));
        RecruitPostResponseDTO responseDTO = RecruitPostMapper.entityToResponseDto(updatedPost);
        return ResponseEntity.ok(responseDTO);
    }


    // 특정 모집 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitPost(@PathVariable Long id,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = Long.valueOf(customUserDetails.getUsername());

        Optional<RecruitPost> recruitPostOpt = recruitPostService.findByIdNotDeleted(id);
        if (recruitPostOpt.isEmpty() || !recruitPostOpt.get().getWriter().getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        recruitPostService.softDelete(id);
        return ResponseEntity.noContent().build();
    }


    // 모집 상태 변경
    @PatchMapping("/{id}/state")
    public ResponseEntity<Void> updatePostState(@PathVariable Long id, @RequestParam Boolean newState) {
        recruitPostService.updateRecruitmentState(id, newState);
        return ResponseEntity.ok().build();
    }

}
