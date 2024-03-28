package com.goormpj.decimal.chat.service;

import com.goormpj.decimal.chat.dto.ChatMessageDto;
import com.goormpj.decimal.chat.dto.VideoChatDto;
import com.goormpj.decimal.chat.mapper.ChatMessageMapper;
import com.goormpj.decimal.chat.model.ChatMessageModel;
import com.goormpj.decimal.chat.model.VideoChatModel;
import com.goormpj.decimal.chat.repository.ChatMessageRepository;
import com.goormpj.decimal.chat.repository.VideoChatRepository;
import com.goormpj.decimal.user.domain.Member;
import com.goormpj.decimal.user.dto.CustomUserDetails;
import com.goormpj.decimal.user.repository.MemberRepository;
import com.goormpj.decimal.user.util.AuthUtils;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VideoChatService {

    @Value("${OPENVIDU_URL}")
    private String openviduUrl;

    @Value("${OPENVIDU_SECRET}")
    private String openviduSecret;

    private OpenVidu openVidu;
    @PostConstruct
    private void init() {
        this.openVidu = new OpenVidu(openviduUrl, openviduSecret);
    }

    private final AuthUtils authUtils;
    private final VideoChatRepository videoChatRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public VideoChatService(VideoChatRepository videoChatRepository, AuthUtils authUtils, MemberRepository memberRepository, ChatMessageRepository chatMessageRepository) {
        this.videoChatRepository = videoChatRepository;
        this.authUtils = authUtils;
        this.memberRepository = memberRepository;
        this.chatMessageRepository = chatMessageRepository;
    }


    public String initializeSession(VideoChatDto videoChatDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws OpenViduJavaClientException, OpenViduHttpException {
        videoChatDto.setIsPublisher(true);
        SessionProperties properties = SessionProperties.fromJson(videoChatDto.getProperties()).build();
        Session session = this.openVidu.createSession(properties);
        String sessionId = session.getSessionId();

        processVideoChatData(videoChatDto, sessionId, customUserDetails);

        return sessionId;
    }


    public String createConnection(String sessionId, VideoChatDto videoChatDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception {
        try {
            Session session = this.openVidu.getActiveSession(sessionId);
             if (session == null) {
                throw new Exception("Session not found");
            }
            ConnectionProperties properties = ConnectionProperties.fromJson(videoChatDto.getProperties()).build();
            Connection connection = session.createConnection(properties);

            processVideoChatData(videoChatDto, sessionId, customUserDetails);

            return connection.getToken();
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            throw new Exception("Failed to create connection: " + e.getMessage(), e);
        }
    }

    // 사용자 초대 로직
    public void inviteUserToSession(String sessionId, String inviteeId) throws UserNotFoundException {
        Member invitee = memberRepository.findById(Long.parseLong(inviteeId))
                .orElseThrow(() -> new UserNotFoundException("Invitee user not found"));

        VideoChatModel videoChatModel = new VideoChatModel();
        videoChatModel.setMember(invitee);
        videoChatModel.setSessionId(sessionId);

        videoChatRepository.save(videoChatModel);
    }

    // 사용자를 세션에서 제거하는 메서드
    public void removeUserFromSession(String sessionId, String userId) throws UserNotFoundException {
        VideoChatModel videoChatModel = videoChatRepository.findBySessionIdAndMemberId(sessionId, Long.parseLong(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found in the session"));

        videoChatRepository.delete(videoChatModel);
    }

    public List<String> getUserSessionsId(Long memberId) {
        List<VideoChatModel> sessions = videoChatRepository.findByMember_Id(memberId);
        List<String> sessionIds = sessions.stream()
                .map(VideoChatModel::getSessionId)
                .collect(Collectors.toList());
        return sessionIds;
    }

    // properties 에서 데이터 추출
    public void processVideoChatData(VideoChatDto videoChatDto, String sessionId, CustomUserDetails customUserDetails) {
        VideoChatModel videoChatModel = new VideoChatModel();
        videoChatModel.setSessionId(sessionId);

        Optional<Member> loggedInMember = authUtils.getLoggedInMember(customUserDetails);
        if (loggedInMember.isPresent()) {
            Member member = loggedInMember.get();
            videoChatModel.setMember(member);
        } else {
            throw new IllegalStateException("No authenticated member found");
        }

        Map<String, Object> properties = videoChatDto.getProperties();
        String customSessionId = (String) properties.get("customSessionId");
        videoChatModel.setCustomSessionId(customSessionId);

        videoChatRepository.save(videoChatModel);
    }

    // 사용자 찾을 수 없음 예외처리
    public class UserNotFoundException extends Exception {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    // 메세지 저장
    public void messageSave(ChatMessageDto chatMessageDto) {
        ChatMessageModel chatMessageModel = ChatMessageMapper.toModel(chatMessageDto);
        chatMessageRepository.save(chatMessageModel);
    }

    // 세션id로 메세지 불러오기
    public List<ChatMessageDto> getMessagesBySessionId(String sessionId) {
        List<ChatMessageModel> messages = chatMessageRepository.findBySession_SessionId(sessionId);

        List<ChatMessageDto> dtoList = messages.stream()
                .map(ChatMessageMapper::toDto)
                .collect(Collectors.toList());

        return dtoList;
    }



}
