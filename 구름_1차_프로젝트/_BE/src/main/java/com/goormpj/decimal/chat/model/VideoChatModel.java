package com.goormpj.decimal.chat.model;

import com.goormpj.decimal.user.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "video_chat") // 테이블 이름 수정
@Getter
@Setter
public class VideoChatModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId; // 세션 ID

    private String customSessionId; // 사용자 정의 세션 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // Member 엔티티와의 관계 명시
    private Member member; // 세션 참가자
}
