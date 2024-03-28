package com.goormpj.decimal.ide.service;

import com.goormpj.decimal.ide.domain.File;
import com.goormpj.decimal.ide.domain.Folder;
import com.goormpj.decimal.ide.domain.Study;
import com.goormpj.decimal.ide.repository.FileRepository;
import com.goormpj.decimal.ide.repository.FolderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;

    @Override
    public void createRootFolderForStudy(Study study) {
        // 최상위 폴더 생성
        Folder newFolder = new Folder();
        newFolder.setFolderName(study.getName()); // 스터디명으로 폴더명 설정
        newFolder.setParentFolder(null); // 최상위 폴더의 부모 폴더는 null 설정
        newFolder.setDepth(0); // 최상위 폴더의 깊이는 0으로 설정
        newFolder.setStudy(study); // 스터디와 연관 설정
        // 최상위 폴더 저장
        folderRepository.save(newFolder);
    }

    @Override
    public Folder createFolderWithFile(String folderName, Long parentId, String fileName) {

        Folder parentFolder = null;
        int depth = 0;

        if (parentId != null) {
            parentFolder = folderRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 폴더를 찾을 수 없습니다."));
            depth = parentFolder.getDepth() + 1;
        }

        Folder newFolder = new Folder(folderName, parentFolder, depth);
        folderRepository.save(newFolder);

        // 파일 생성
        if (fileName != null && !fileName.isEmpty()) {
            File newStudyFile = new File();
            newStudyFile.setFileName(fileName);
            fileRepository.save(newStudyFile);

            newFolder.getStudyFiles().add(newStudyFile);
            folderRepository.save(newFolder);
        }

        return newFolder;
    }

    @Override
    public Folder updateFolder(Long folderId, Long parentId) {
        Folder folder = folderRepository.findById(folderId).orElse(null);

        if (folder == null) {
            return null;
        }

        Folder parentFolder = folderRepository.findById(parentId).orElse(null);

        int depth;
        if (parentFolder != null) {
            depth = parentFolder.getDepth() + 1;
        } else {
            depth = 0;
        }

        folder.setParentFolder(parentFolder);
        folder.setDepth(depth);

        return folderRepository.save(folder);
    }

    @Override
    public void deleteFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId).orElse(null);
        if (folder != null) {
            deleteFolderRecursively(folder);
        }
    }

    private void deleteFolderRecursively(Folder folder) {
        List<Folder> childFolders = folder.getChildFolders();
        for (Folder childFolder : childFolders) {
            deleteFolderRecursively(childFolder);
        }
        folderRepository.delete(folder);
    }

    @Override
    public List<Folder> getAllFolders() {
        return folderRepository.findByParentFolderIsNull();
    }

    @Override
    public List<Folder> getChildFolders(Long parentId) {
        Folder parentFolder = folderRepository.findById(parentId).orElse(null);
        if (parentFolder == null) {
            return Collections.emptyList();
        }

        return parentFolder.getChildFolders();
    }
}
