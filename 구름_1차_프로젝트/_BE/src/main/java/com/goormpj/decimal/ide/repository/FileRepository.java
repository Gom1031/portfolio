package com.goormpj.decimal.ide.repository;

import com.goormpj.decimal.ide.domain.File;
import com.goormpj.decimal.ide.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByFolder(Folder folder);

}
