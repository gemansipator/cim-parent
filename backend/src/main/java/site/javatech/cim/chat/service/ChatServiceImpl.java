/**
 * Реализация сервиса для управления чатом.
 * Обрабатывает отправку, получение и удаление сообщений.
 */
package site.javatech.cim.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.javatech.cim.chat.model.ChatMessage;
import site.javatech.cim.chat.repository.ChatMessageRepository;
import site.javatech.cim.core.model.User;
import site.javatech.cim.core.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public ChatMessage sendMessage(ChatMessage message) {
        // Проверка статуса пользователя
        User user = userService.getUserByUsername(message.getSenderUsername());
        if (user.getStatus() != User.Status.APPROVED) {
            throw new IllegalStateException("Пользователь не может отправлять сообщения (статус: " + user.getStatus() + ")");
        }
        message.setTimestamp(LocalDateTime.now());
        message.setDeleted(false);
        chatMessageRepository.save(message);
        // Удаление старых сообщений, если превышен лимит
        chatMessageRepository.deleteMessagesBeyondLimit();
        return message;
    }

    @Override
    public Page<ChatMessage> getMessages(Pageable pageable) {
        return chatMessageRepository.findByDeletedFalseOrderByTimestampDesc(pageable);
    }

    @Override
    @Transactional
    public ChatMessage deleteMessage(Long id, String username, boolean isAdmin) {
        ChatMessage message = chatMessageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сообщение не найдено"));
        if (message.isDeleted()) {
            throw new IllegalStateException("Сообщение уже удалено");
        }
        // Проверка прав на удаление
        if (!isAdmin && !message.getSenderUsername().equals(username)) {
            throw new IllegalStateException("Вы можете удалять только свои сообщения");
        }
        if (!isAdmin && message.getTimestamp().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new IllegalStateException("Время для удаления сообщения истекло");
        }
        message.setDeleted(true);
        return chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessage> getLatestMessages() {
        return chatMessageRepository.findByDeletedFalseOrderByTimestampDesc(Pageable.ofSize(100)).getContent();
    }
}