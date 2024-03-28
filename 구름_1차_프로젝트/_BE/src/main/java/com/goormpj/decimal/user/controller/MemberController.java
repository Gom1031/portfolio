package com.goormpj.decimal.user.controller;

import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.dto.NicknameRequestDTO;
import com.goormpj.decimal.user.dto.CustomUserDetails;
import com.goormpj.decimal.user.dto.PasswordRequestDTO;
import com.goormpj.decimal.user.dto.SignUpRequestDTO;
import com.goormpj.decimal.user.service.MemberService;
import com.goormpj.decimal.user.util.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    private final AuthUtils authUtils;

    //로그인한 회원 정보 가져오기 예시
    @PostMapping("/memberProfile")
    public ResponseEntity<Member> getMemberProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Optional<Member> loggedInMember = authUtils.getLoggedInMember(customUserDetails);
        //return ResponseEntity.ok().body(customUserDetails.getUsername());
        return ResponseEntity.ok().body(loggedInMember.orElse(null));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        //이메일 중복 확인
        if(memberService.existsByEmail(signUpRequestDTO.getEmail())){
            return ResponseEntity.badRequest().body("이메일 중복 오류");
        }

        //닉네임 중복 확인
        if(memberService.existsByNickname(signUpRequestDTO.getNickname())){
            return ResponseEntity.badRequest().body("닉네임 중복 오류");
        }

        // Validation 통과 시 회원가입 로직 수행
        memberService.save(signUpRequestDTO);

        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/checkNickname")
    public ResponseEntity<String> checkNickname(@Valid @RequestBody NicknameRequestDTO nicknameRequestDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        if(memberService.existsByNickname(nicknameRequestDTO.getNickname())) {
            return ResponseEntity.badRequest().body("닉네임 중복 오류");
        }else{
            return ResponseEntity.ok().body("닉네임 사용 가능");
        }
    }

    @PostMapping("/updateNickname")
    public ResponseEntity<String> updateeNickname(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody NicknameRequestDTO nicknameRequestDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        //중복 확인
        if(memberService.existsByNickname(nicknameRequestDTO.getNickname())) {
            return ResponseEntity.badRequest().body("닉네임 중복 오류");
        }else{
            Long id = Long.parseLong(customUserDetails.getUsername());
            memberService.updateNickname(id , nicknameRequestDTO);
            return ResponseEntity.ok().body("닉네임 변경 완료");
        }
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal CustomUserDetails customUserDetails,@Valid @RequestBody PasswordRequestDTO passwordRequestDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        Long id = Long.parseLong(customUserDetails.getUsername());

        Boolean result = memberService.updatePassword(id, passwordRequestDTO);
        if(result) {
            return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
        }else if(!result){
            return ResponseEntity.badRequest().body("기존 비밀번호가 일치하지 않습니다.");
        }else{
            return ResponseEntity.badRequest().body("로그인 정보가 존재하지 않습니다.");
        }
    }

    private ResponseEntity<String> handleValidationErrors(BindingResult bindingResult) {
        StringBuilder errors = new StringBuilder();
        bindingResult.getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.append(errorMessage).append("\n");
        });
        return ResponseEntity.badRequest().body(errors.toString());
    }

}
