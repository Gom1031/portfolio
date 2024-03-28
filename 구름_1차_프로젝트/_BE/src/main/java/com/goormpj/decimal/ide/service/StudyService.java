package com.goormpj.decimal.ide.service;


import com.goormpj.decimal.board.dto.RecruitInfoDTO;
import com.goormpj.decimal.board.dto.RecruitPostRequestDTO;
import com.goormpj.decimal.board.entity.RecruitPost;
import com.goormpj.decimal.ide.domain.Folder;
import com.goormpj.decimal.ide.domain.Study;

public interface StudyService {

    Study createStudy(RecruitPostRequestDTO requestDTO, RecruitInfoDTO recruitInfoDTO); // 스터디 생성 및 최상위 폴더 생성

    void createRootFolderForStudy(Study study); // 스터디명으로 최상위 폴더 생성

    Study getStudyById(Long id); // id로 스터디 찾기


}
