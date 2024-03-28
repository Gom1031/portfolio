package com.goormpj.decimal.ide.repository;

import com.goormpj.decimal.ide.domain.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
//    boolean existsByStudyIdAndMemberIdAndRole(Long studyId, Long memberId, Role role);
}
