/**
 * Репозиторий для работы с сообщениями чата в базе данных.
 * Добавлены методы для пагинации и удаления старых сообщений.
 */
package site.javatech.cim.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import site.javatech.cim.chat.model.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Получить последние сообщения с пагинацией
    Page<ChatMessage> findByDeletedFalseOrderByTimestampDesc(Pageable pageable);

    // Удалить сообщения старше 5000-го
    @Modifying
    @Query(value = "DELETE FROM chat_messages WHERE id IN " +
            "(SELECT id FROM chat_messages WHERE deleted = false ORDER BY timestamp ASC LIMIT 1 OFFSET 5000)", nativeQuery = true)
    void deleteMessagesBeyondLimit();
}