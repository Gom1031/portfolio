package com.goormpj.decimal.user.service;

import com.goormpj.decimal.user.domain.Authority;
import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.dto.LoginRequestDTO;
import com.goormpj.decimal.user.dto.NicknameRequestDTO;
import com.goormpj.decimal.user.dto.PasswordRequestDTO;
import com.goormpj.decimal.user.dto.SignUpRequestDTO;
import com.goormpj.decimal.user.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //User save(SignUpRequestDTO signUpRequestDTO); 회원가입
    @Override
    public Member save(SignUpRequestDTO signUpRequestDTO) {
        Member member = Member.builder()
                .email(signUpRequestDTO.getEmail())
                .nickname(signUpRequestDTO.getNickname())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();
        return memberRepository.save(member);
    }

    //Boolean existsByEmail(String email); 이메일 존재 체크
    @Override
    public Boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    //Boolean exisexistsByNickname(String nickname); 닉네임 중복 확인
    @Override
    public Boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    //Member findByEmail(String email); 이메일로 회원 정보 조회
    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    //void login(String email, String password); 로그인
    @Override
    public Boolean passwordCheck(LoginRequestDTO loginRequestDTO) {
        Member member = memberRepository.findByEmail(loginRequestDTO.getEmail());

        if(passwordEncoder.matches(loginRequestDTO.getPassword(),member.getPassword()))
            return true;
        else
            return false;
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    //Boolean updatePassword(Long id, PasswordRequestDTO passwordRequestDTO); 비밀번호 변경
    @Override
    public Boolean updatePassword(Long id, PasswordRequestDTO passwordRequestDTO) {
        Member member = memberRepository.findById(id).orElse(null);
        boolean matches = passwordEncoder.matches(passwordRequestDTO.getPassword(), member.getPassword());

        if(!matches)
            return false;
        else{
            member.updatePassword(passwordEncoder.encode(passwordRequestDTO.getNewPassword()));
            return true;
        }

    }

    //void updateNickname(Long id, NicknameRequestDTO nicknameRequestDTO); 닉네임 변경
    @Override
    public void updateNickname(Long id, NicknameRequestDTO nicknameRequestDTO) {
        Member member = memberRepository.findById(id).orElse(null);
        member.updateNickname(nicknameRequestDTO.getNickname());
    }


}
