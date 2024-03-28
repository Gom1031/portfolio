package com.goormpj.decimal.ide.service;

import com.goormpj.decimal.ide.dto.CodeChangeDto;
import com.goormpj.decimal.ide.mapper.CodeChangeMapper;
import com.goormpj.decimal.ide.domain.CodeChangeModel;
import com.goormpj.decimal.ide.repository.CodeChangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodeChangeServiceImpl implements CodeChangeService {

    private final CodeChangeRepository codeChangeRepository;

    @Override
    public CodeChangeDto processChange(CodeChangeDto dto) {
        // 가장 최근의 변경사항을 DB에서 가져와 비교
        Optional<CodeChangeModel> lastChange = codeChangeRepository.findTopByFileIdOrderByTimestampDesc(dto.getFileId());
        CodeChangeModel newChange = CodeChangeMapper.dtoToEntity(dto);

        if (!lastChange.isPresent() || newChange.getTimestamp() > lastChange.get().getTimestamp()) {
            // 새로운 변경사항 저장
            codeChangeRepository.save(newChange);
            return dto;
        }

        // 이전 변경사항 반환
        return CodeChangeMapper.entityToDto(lastChange.get());
    }
}
