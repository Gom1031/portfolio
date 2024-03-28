package com.goormpj.decimal.chat.controller;

import com.goormpj.decimal.chat.dto.ChatMessageDto;
import com.goormpj.decimal.chat.dto.VideoChatDto;
import com.goormpj.decimal.chat.service.VideoChatService;
import com.goormpj.decimal.user.dto.CustomUserDetails;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class VideoChatController {

    private final VideoChatService videoChatService;
    private static final Logger log = LoggerFactory.getLogger(VideoChatController.class);

    @Autowired
    public VideoChatController(VideoChatService videoChatService) {
        this.videoChatService = videoChatService;
    }

    // 세션 생성
    @PostMapping("/sessions")
    public ResponseEntity<String> initializeSession(@RequestBody VideoChatDto videoChatDto,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (videoChatDto == null) {
            // videoChatDto가 null인 경우의 에러 메시지
            return new ResponseEntity<>("Request body (videoChatDto) is null", HttpStatus.BAD_REQUEST);
        }

        if (videoChatDto.getProperties() == null) {
            // properties 필드가 null인 경우의 에러 메시지
            return new ResponseEntity<>("'properties' field in videoChatDto is null", HttpStatus.BAD_REQUEST);
        }

        try {
            String sessionId = videoChatService.initializeSession(videoChatDto, customUserDetails);
            return ResponseEntity.ok(sessionId);
        } catch (OpenViduJavaClientException e) {
            // OpenViduJavaClientException 발생 시 처리
            return new ResponseEntity<>("OpenVidu Java Client error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (OpenViduHttpException e) {
            // OpenViduHttpException 발생 시 처리
            if (e.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                return new ResponseEntity<>("OpenVidu authorization error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>("OpenVidu HTTP error: " + e.getMessage(), HttpStatus.valueOf(e.getStatus()));
            }
        } catch (Exception e) {
            // 기타 예외 처리
            return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 세션 연결
    @PostMapping("/sessions/{sessionId}/connections")
    public ResponseEntity<?> createConnection(@PathVariable("sessionId") String sessionId,
                                              @RequestBody(required = false) VideoChatDto videoChatDto,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (sessionId == null || sessionId.isEmpty()) {
            // sessionId 유효성 검증 실패
            return new ResponseEntity<>("Session ID is missing or invalid.", HttpStatus.BAD_REQUEST);
        }

        if (videoChatDto == null) {
            // videoChatDto가 null인 경우
            return new ResponseEntity<>("Request body is missing.", HttpStatus.BAD_REQUEST);
        }

        try {
            String token = videoChatService.createConnection(sessionId, videoChatDto, customUserDetails);
            return ResponseEntity.ok(token);
        } catch (OpenViduJavaClientException e) {
            // OpenVidu Java 클라이언트 관련 예외 처리
            return new ResponseEntity<>("Error while communicating with OpenVidu Java Client: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (OpenViduHttpException e) {
            // OpenVidu HTTP 에러 처리
            return new ResponseEntity<>("OpenVidu server error with HTTP status " + e.getStatus() + ": " + e.getMessage(), HttpStatus.valueOf(e.getStatus()));
        } catch (IllegalArgumentException e) {
            // 잘못된 인자 예외 처리
            return new ResponseEntity<>("Invalid argument: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // 기타 예외 처리
            return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 특정 사용자를 session에 초대(추가)
    @PostMapping("/sessions/{sessionId}/invite")
    public ResponseEntity<?> inviteUserToSession(
            @PathVariable("sessionId") String sessionId,
            @RequestParam String inviteeId)
    {
        try {
            videoChatService.inviteUserToSession(sessionId, inviteeId);

            return ResponseEntity.ok("User invited successfully to the session.");
        } catch (Exception e) {
            // 실패 응답 반환
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 세션에서 사용자를 제거(삭제)
    @DeleteMapping("/sessions/{sessionId}/leave")
    public ResponseEntity<?> leaveSession(
            @PathVariable("sessionId") String sessionId,
            @RequestParam String userId)
    {
        try {
            videoChatService.removeUserFromSession(sessionId, userId);

            return ResponseEntity.ok("User removed successfully from the session.");
        } catch (Exception e) {
            // 실패 응답 반환
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 사용자가 연결된 모든 세션 가져오기
    @GetMapping("/mysessions")
    public ResponseEntity<List<String>> getUserSessions(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null || customUserDetails.getUsername() == null) {
            log.warn("Unauthorized access attempt.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Long memberId;
        try {
            memberId = Long.parseLong(customUserDetails.getUsername());
        } catch (NumberFormatException e) {
            log.error("Error parsing memberId from username: {}", customUserDetails.getUsername(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            List<String> sessions = videoChatService.getUserSessionsId(memberId);
            if (sessions.isEmpty()) {
                log.info("No sessions found for memberId: {}", memberId);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            log.error("Internal server error while retrieving sessions for memberId: {}", memberId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 메세지 저장하기
    @MessageMapping("{sessionId}/chat.sendMessage/")
    public void sendMessage(@PathVariable("sessionId") String sessionId,
                            @Payload ChatMessageDto chatMessageDto) {
        chatMessageDto.setSessionId(sessionId);
        videoChatService.messageSave(chatMessageDto);
    }

    // 세션에서 메세지 불러오기
    @MessageMapping("{sessionId}/chat.getMessages/")
    public List<ChatMessageDto> getMessages(@PathVariable("sessionId") String sessionId) {
        return videoChatService.getMessagesBySessionId(sessionId);
    }

}
