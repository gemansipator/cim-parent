/**
 * WebSocket контроллер для обработки сообщений чата.
 */
package site.javatech.cim.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import site.javatech.cim.chat.model.ChatMessage;
import site.javatech.cim.chat.service.ChatService;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage message) {
        return chatService.sendMessage(message);
    }

    @MessageMapping("/chat.deleteMessage")
    @SendTo("/topic/public")
    public ChatMessage deleteMessage(ChatMessage message) {
        return chatService.deleteMessage(message.getId(), message.getSenderUsername(), false);
    }
}