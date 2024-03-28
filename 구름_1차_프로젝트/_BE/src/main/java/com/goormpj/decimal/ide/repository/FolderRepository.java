package com.goormpj.decimal.ide.repository;

import com.goormpj.decimal.ide.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByParentFolderIsNull(); // 부모 폴더가 없는 최상위 폴더 검색

    List<Folder> findByParentFolder(Folder parentFolder); // 부모 폴더에 속하는 하위 폴더 검색

}
