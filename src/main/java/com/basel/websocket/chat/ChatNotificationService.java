package com.basel.websocket.chat;

import org.springframework.stereotype.Service;

@Service
public class ChatNotificationService {

    public ChatNotification mapChatMessageToChatNotification(ChatMessage chatMessage) {
        return ChatNotification.builder()
                .id(chatMessage.getId())
                .senderId(chatMessage.getSenderId())
                .recipientId(chatMessage.getRecipientId())
                .content(chatMessage.getContent())
                .build();
    }

}
