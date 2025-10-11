/**
 * Модель сообщения чата.
 * Хранит текст сообщения, отправителя, время, ответ на другое сообщение и флаг удаления.
 * Поля для будущих личных сообщений и комнат (recipientId, roomId) добавлены как заготовки.
 */
package site.javatech.cim.chat.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "chat_messages")
@Getter
@Setter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String senderUsername;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column
    private Long replyToId; // ID сообщения, на которое отвечают (null для обычных сообщений)

    @Column
    private Long recipientId; // Заготовка для личных сообщений (null для общего чата)

    @Column
    private Long roomId; // Заготовка для комнат (null для общего чата)

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean deleted = false; // Флаг удаления сообщения
}