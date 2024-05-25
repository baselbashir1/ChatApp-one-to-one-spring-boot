package com.basel.websocket.chatroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatIdFromChatRoom(String senderId, String recipientId, boolean createNewRoomIfNotExists) {
        try {
            return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                    .map(ChatRoom::getChatId)
                    .or(() -> {
                        if (createNewRoomIfNotExists) {
                            String chatId = createChatId(senderId, recipientId);
                            return Optional.of(chatId);
                        }
                        return Optional.empty();
                    });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String createChatId(String senderId, String recipientId) {
        try {
            String chatId = String.format("%s_%s", senderId, recipientId); // basel_ahmad

            ChatRoom senderRecipient = ChatRoom.builder()
                    .chatId(chatId)
                    .senderId(senderId)
                    .recipientId(recipientId)
                    .build();

            ChatRoom recipientSender = ChatRoom.builder()
                    .chatId(chatId)
                    .senderId(recipientId)
                    .recipientId(senderId)
                    .build();

            chatRoomRepository.save(senderRecipient);
            chatRoomRepository.save(recipientSender);

            return chatId;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
