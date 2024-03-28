package com.goormpj.decimal.ide.controller;

import com.goormpj.decimal.ide.dto.CodeChangeDto;
import com.goormpj.decimal.ide.service.CodeChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class CodeEditorController {

    private final CodeChangeService codeChangeService;

    @MessageMapping("/change")
    @SendTo("/topic/changes")
    public CodeChangeDto handleTextChange(CodeChangeDto dto) {
        return codeChangeService.processChange(dto);
    }
}
