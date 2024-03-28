package com.goormpj.decimal.ide.domain;

import com.goormpj.decimal.board.entity.RecruitInfo;
import com.goormpj.decimal.board.entity.RecruitPost;
import com.goormpj.decimal.user.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Study {

    @Id @GeneratedValue
    @Column(name = "study_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private Member member;

    private Boolean isLeader;

    private String name;

    private int memberCount;

    @OneToMany(mappedBy = "study")
    private List<StudyMember> members = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_post_id")
    private RecruitPost recruitPost;

    public Study() {
    }

}