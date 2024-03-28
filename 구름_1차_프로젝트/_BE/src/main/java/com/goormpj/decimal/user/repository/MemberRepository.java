package com.goormpj.decimal.user.repository;

import com.goormpj.decimal.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByEmail(String id); // 이메일 존재 체크

    Boolean existsByNickname(String nickname); // 닉네임 존재 체크

    Member findByEmail(String email); // 이메일로 회원 정보 조회

}
