package com.goormpj.decimal.chat.repository;

import com.goormpj.decimal.chat.model.ChatMessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageModel, Long> {
    List<ChatMessageModel> findBySession_SessionId(String sessionId);

}
