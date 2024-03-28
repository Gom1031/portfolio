package com.goormpj.decimal.board.repository;

import com.goormpj.decimal.board.entity.RecruitInfo;
import com.goormpj.decimal.board.entity.RecruitPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RecruitPostRepository extends JpaRepository<RecruitPost, Long> {
        List<RecruitPost> findByIsDeletedFalse();   // 소프트 삭제되지 않는 모든 게시글 조회
        List<RecruitPost> findByWriterIdAndIsDeletedFalse(Long memberId);

        Optional<RecruitPost> findByIdAndIsDeletedFalse(Long id); // 수정됨
}
