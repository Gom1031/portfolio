package com.goormpj.decimal.board.entity;

import com.goormpj.decimal.user.domain.Member;  // 경로 변경
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RecruitPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 ID

    @ManyToOne
    private Member writer;

    private String title; // 제목

    @Column(columnDefinition = "LONGTEXT")
    private String content; // 내용

    private Integer applied = 0; // 현재까지 지원한 인원
    private Integer recruited; // 총 모집 인원

    private Boolean state = true; // 모집 상태
    private String target; // 모집 대상

    @CreationTimestamp
    @Column(name = "Created_at", updatable = false)
    private LocalDateTime createdAt; // 작성 시각

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private Boolean isDeleted = false;

    // 생성자 추가
    public RecruitPost(String title, String content, Member writer, boolean isDeleted, Integer recruited,
                       Boolean state, String target) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.isDeleted = isDeleted;
        this.recruited = recruited;
        this.state = state;
        this.target = target;
    }


}