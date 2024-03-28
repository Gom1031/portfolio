package com.goormpj.decimal.board.repository;

import com.goormpj.decimal.board.entity.RecruitInfo;
import com.goormpj.decimal.board.entity.RecruitPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecruitInfoRepository extends JpaRepository<RecruitInfo, Long> {
    List<RecruitInfo> findByRecruitPostIdAndIsDeletedFalse(Long recruitPostId);
    List<RecruitInfo> findByMemberIdAndIsDeletedFalse(Long memberId);

    // 지원자 ID를 기반으로, 삭제되지 않은 모든 RecruitPost 찾기
    @Query("SELECT ri.recruitPost FROM RecruitInfo ri WHERE ri.member.id = :memberId AND ri.isDeleted = false")
    List<RecruitPost> findRecruitPostsByApplicantId(@Param("memberId") Long memberId);

}