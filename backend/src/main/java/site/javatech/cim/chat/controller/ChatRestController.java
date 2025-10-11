/**
 * REST-контроллер для управления сообщениями чата и статусами пользователей.
 */
package site.javatech.cim.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.javatech.cim.chat.model.ChatMessage;
import site.javatech.cim.chat.service.ChatService;
import site.javatech.cim.core.model.UserStatus;
import site.javatech.cim.core.service.UserStatusService;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserStatusService userStatusService;

    @GetMapping("/messages")
    public ResponseEntity<Page<ChatMessage>> getMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ResponseEntity.ok(chatService.getMessages(PageRequest.of(page, size)));
    }

    @PostMapping("/user-statuses")
    public ResponseEntity<Void> updateUserStatus(@RequestBody UserStatus status) {
        userStatusService.updateUserStatus(status.getUserId(), status.isOnline());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user-statuses")
    public ResponseEntity<Iterable<UserStatus>> getUserStatuses() {
        return ResponseEntity.ok(userStatusService.getAllUserStatuses());
    }
}