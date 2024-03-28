package com.goormpj.decimal.ide.controller;

import com.goormpj.decimal.ide.domain.Folder;
import com.goormpj.decimal.ide.dto.FolderRequestDTO;
import com.goormpj.decimal.ide.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ide")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    // 새 폴더 생성
    @PostMapping("/studies")
    public ResponseEntity<Folder> createFolder(@RequestBody FolderRequestDTO folderRequestDTO) {

        Folder newFolder = folderService.createFolderWithFile(folderRequestDTO.getFolderName(), folderRequestDTO.getParentId(), folderRequestDTO.getFileName());

        if (newFolder != null) {
            return new ResponseEntity<>(newFolder, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    // 기존 폴더 정보 업데이트
    @PutMapping("/{folderId}")
    public ResponseEntity<Folder> updateFolder(@PathVariable Long folderId, @RequestParam Long parentId) {
        Folder updatedFolder = folderService.updateFolder(folderId, parentId);

        if (updatedFolder != null) {
            return new ResponseEntity<>(updatedFolder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    // 특정 폴더 삭제
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long folderId) {
        folderService.deleteFolder(folderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 모든 폴더 조회
    @GetMapping("/studies")
    public ResponseEntity<List<Folder>> getAllFolders() {
        List<Folder> folders = folderService.getAllFolders();
        return new ResponseEntity<>(folders, HttpStatus.OK);
    }

    // 특정 부모 ID를 가진 자식 폴더들을 조회
    @GetMapping("/{parentId}/childFolders")
    public ResponseEntity<List<Folder>> getChildFolders(@PathVariable Long parentId) {
        List<Folder> subFolders = folderService.getChildFolders(parentId);
        return new ResponseEntity<>(subFolders, HttpStatus.OK);
    }
}
