package com.goormpj.decimal.ide.dto;

import com.goormpj.decimal.user.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyResponseDTO {

    private Long id;
    private Boolean isLeader;
    private String name;
    private int memberCount;

}
