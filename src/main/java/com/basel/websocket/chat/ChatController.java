package com.basel.websocket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatNotificationService chatNotificationService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMessage = chatMessageService.save(chatMessage);
        ChatNotification chatNotification = chatNotificationService.mapChatMessageToChatNotification(savedMessage);
        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId(), "/queue/messages", chatNotification);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<Object> findChatMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        try {
            return new ResponseEntity<>(chatMessageService.findChatMessages(senderId, recipientId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        }
    }

}
