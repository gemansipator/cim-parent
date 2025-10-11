/**
 * Интерфейс сервиса для управления чатом.
 */
package site.javatech.cim.chat.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.javatech.cim.chat.model.ChatMessage;

import java.util.List;

public interface ChatService {
    ChatMessage sendMessage(ChatMessage message);

    Page<ChatMessage> getMessages(Pageable pageable);

    ChatMessage deleteMessage(Long id, String username, boolean isAdmin);

    List<ChatMessage> getLatestMessages();
}