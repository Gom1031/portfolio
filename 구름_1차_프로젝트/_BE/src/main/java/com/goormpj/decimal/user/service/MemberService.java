package com.goormpj.decimal.user.service;

import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.dto.LoginRequestDTO;
import com.goormpj.decimal.user.dto.NicknameRequestDTO;
import com.goormpj.decimal.user.dto.PasswordRequestDTO;
import com.goormpj.decimal.user.dto.SignUpRequestDTO;

public interface MemberService {

    Member save(SignUpRequestDTO signUpRequestDTO); // 회원가입

    Boolean existsByEmail(String email);//이메일 존재 체크

    Boolean existsByNickname(String nickname); // 닉네임 중복 확인

    Member findByEmail(String email); //이메일로 회원 정보 조회

    Boolean passwordCheck(LoginRequestDTO loginRequestDTO); // 로그인

    Member findById(Long id);//id로 회원 정보 조회

    Boolean updatePassword(Long id, PasswordRequestDTO passwordRequestDTO);//비밀번호 변경

    void updateNickname(Long id, NicknameRequestDTO nicknameRequestDTO);//닉네임 변경


}
