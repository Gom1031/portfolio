package com.goormpj.decimal.ide.controller;

import com.goormpj.decimal.board.dto.RecruitInfoDTO;
import com.goormpj.decimal.board.dto.RecruitPostDTO;
import com.goormpj.decimal.board.dto.RecruitPostRequestDTO;
import com.goormpj.decimal.board.entity.RecruitPost;
import com.goormpj.decimal.ide.domain.Folder;
import com.goormpj.decimal.ide.domain.Study;
import com.goormpj.decimal.ide.dto.StudyRequestDTO;
import com.goormpj.decimal.ide.dto.StudyResponseDTO;
import com.goormpj.decimal.ide.mapper.StudyMapper;
import com.goormpj.decimal.ide.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ide")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    // 스터디 생성 시 최상위 폴더 생성
    // 스터디 상세 정보 조회 및 화면에 표시

    @PostMapping("/study")
    public ResponseEntity<String> createStudyFromRecruitPost(@RequestBody RecruitPostRequestDTO requestDTO, RecruitInfoDTO recruitInfoDTO) {
        Study newStudy = studyService.createStudy(requestDTO, recruitInfoDTO);

        if (newStudy != null) {
            studyService.createRootFolderForStudy(newStudy);
            StudyResponseDTO responseDTO = StudyMapper.entityToResponseDto(newStudy);
            return ResponseEntity.ok("스터디가 생성되었습니다. 스터디 ID: " + responseDTO.getId());
        } else {
            return ResponseEntity.badRequest().body("스터디 생성에 실패했습니다.");
        }
    }
    @GetMapping("/study/{id}")
    public ResponseEntity<StudyResponseDTO> getStudyDetails(@PathVariable Long id) {
        Study study = studyService.getStudyById(id);
        if (study != null) {
            StudyResponseDTO studyResponseDTO = new StudyResponseDTO();
            studyResponseDTO.setName(study.getName());
            studyResponseDTO.setMemberCount(study.getMemberCount());
            // 다른 필드 설정

            return new ResponseEntity<>(studyResponseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
