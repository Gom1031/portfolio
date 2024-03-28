package com.goormpj.decimal.ide.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // 순환 참조 제거
public class Folder {

    @Id @GeneratedValue
    @Column(name = "folder_id")
    private Long id;

    private String folderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL)
    private List<Folder> childFolders;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<File> studyFiles = new ArrayList<>();

    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    public Folder() {
    }

    public Folder(String folderName, Folder parentFolder, int depth) {
        this.folderName = folderName;
        this.parentFolder = parentFolder;
        this.depth = depth;
    }
}
