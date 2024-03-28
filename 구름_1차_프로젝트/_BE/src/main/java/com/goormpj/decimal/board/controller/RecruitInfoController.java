package com.goormpj.decimal.board.controller;

import com.goormpj.decimal.board.dto.RecruitApplicationDTO;
import com.goormpj.decimal.board.dto.RecruitInfoDTO;
import com.goormpj.decimal.board.dto.RecruitPostResponseDTO;
import com.goormpj.decimal.board.entity.RecruitInfo;
import com.goormpj.decimal.board.mapper.RecruitInfoMapper;
import com.goormpj.decimal.board.service.RecruitInfoService;
import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.dto.CustomUserDetails;
import com.goormpj.decimal.user.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruitInfo")
public class RecruitInfoController {
    private final RecruitInfoService recruitInfoService;

    @Autowired
    public RecruitInfoController(RecruitInfoService recruitInfoService) {
        this.recruitInfoService = recruitInfoService;
    }

    // 지원 게시글 생성
    @PostMapping("/{parentPostId}")
    public ResponseEntity<RecruitInfoDTO> createRecruitInfo(@PathVariable("parentPostId") Long parentPostId,
                                                            @RequestBody RecruitInfoDTO recruitInfoDTO,
                                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        RecruitInfoDTO createdRecruitInfoDTO = recruitInfoService.createRecruitInfo(recruitInfoDTO, parentPostId, customUserDetails);
        return new ResponseEntity<>(createdRecruitInfoDTO, HttpStatus.CREATED);
    }

    // 부모 게시글에 딸린 지원 게시글 모두 불러오기
    @GetMapping("/{parentPostId}")
    public ResponseEntity<List<RecruitInfoDTO>> getRecruitInfosByParentPostId(@PathVariable Long parentPostId) {
        List<RecruitInfoDTO> recruitInfoDTOs = recruitInfoService.getRecruitInfosByParentPostId(parentPostId);
        return ResponseEntity.ok(recruitInfoDTOs);
    }

    // 특정 유저가 작성한 모든 지원 게시글 불러오기
    @GetMapping("/myPost")
    public ResponseEntity<List<RecruitInfoDTO>> getMyRecruitInfos(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<RecruitInfoDTO> myRecruitInfoDTOs = recruitInfoService.getMyRecruitInfos(customUserDetails);
        return ResponseEntity.ok(myRecruitInfoDTOs);
    }

    // 특정 유저가 지원한 모든 모집 게시글 불러오기
    @GetMapping("/myApply")
    public ResponseEntity<List<RecruitApplicationDTO>> getMyRecruitPost(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<RecruitApplicationDTO> myRecruitInfoDTOs = recruitInfoService.getMyRecruitPost(customUserDetails);
        return ResponseEntity.ok(myRecruitInfoDTOs);
    }

    // 모집 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitInfo(@PathVariable Long id) {
        recruitInfoService.deleteRecruitInfo(id);
        return ResponseEntity.ok().build();
    }

    // 지원 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<RecruitInfoDTO> updateRecruitInfo(@PathVariable Long id, @RequestBody RecruitInfoDTO recruitInfoDTO) {
        RecruitInfoDTO updatedRecruitInfoDTO = recruitInfoService.updateRecruitInfo(id, recruitInfoDTO);
        return ResponseEntity.ok(updatedRecruitInfoDTO);
    }

    // 지원 수락 혹은 거절
    @PatchMapping("/{id}/response")
    public ResponseEntity<Void> respondToRecruit(@PathVariable Long id, @RequestParam("accept") boolean accept) {
        recruitInfoService.respondToRecruit(id, accept);
        return ResponseEntity.ok().build();
    }

}


