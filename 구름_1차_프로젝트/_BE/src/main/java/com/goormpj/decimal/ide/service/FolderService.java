package com.goormpj.decimal.ide.service;

import com.goormpj.decimal.ide.domain.Folder;
import com.goormpj.decimal.ide.domain.Study;

import java.util.List;

public interface FolderService {

    void createRootFolderForStudy(Study study); // 최상위 폴더 생성

    Folder createFolderWithFile(String folderName, Long parentId, String fileName); // 폴더 생성 시 파일 자동 생성

    Folder updateFolder(Long folderId, Long parentId); // 폴더 수정

    void deleteFolder(Long folderId); // 폴더 삭제

    List<Folder> getAllFolders(); // 상위 폴더 찾기

    List<Folder> getChildFolders(Long parentId); // 하위 폴더 찾기

}
