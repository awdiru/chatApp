package com.example.messenger.message;

import com.example.messenger.exceptions.handler.UserNotFoundException;
import com.example.template.model.message.ChatMessage;
import com.example.template.model.message.dto.model.ChatMessageDto;
import com.example.template.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
        User toUser = userRepository.findByName(chatMessageDto.getToUser())
                .orElseThrow(() -> new UserNotFoundException("User not found with name: " + chatMessageDto.getToUser()));

        User fromUser = userRepository.findByName(chatMessageDto.getFromUser())
                .orElseThrow(() -> new UserNotFoundException("User not found with name: " + chatMessageDto.getFromUser()));

        ChatMessage chatMessage = ChatMessage.builder()
                .time(LocalDateTime.now())
                .toUser(toUser)
                .fromUser(fromUser)
                .message(chatMessageDto.getMessage())
                .build();
        messageRepository.save(chatMessage);
        chatMessageDto.setTime(chatMessage.getTime());

        return chatMessageDto;
    }

    public void sendToRecipient(ChatMessageDto message) {
        String recipient = message.getToUser();
        String destination = "/topic/user/" + recipient;

        messagingTemplate.convertAndSend(destination, message);
    }
}
