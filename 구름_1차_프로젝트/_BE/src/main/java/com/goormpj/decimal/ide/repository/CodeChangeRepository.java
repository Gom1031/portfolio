package com.goormpj.decimal.ide.repository;

import com.goormpj.decimal.ide.domain.CodeChangeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeChangeRepository extends JpaRepository<CodeChangeModel, Long> {
    Optional<CodeChangeModel> findTopByFileIdOrderByTimestampDesc(Long fileId);
}
