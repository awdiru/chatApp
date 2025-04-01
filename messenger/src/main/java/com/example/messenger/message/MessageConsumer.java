package com.example.messenger.message;

import com.example.template.model.message.dto.model.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageConsumer {
    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = "chat-messages", groupId = "messenger-group")
    public void consume(ChatMessageDto chatMessageDto) {
        log.debug("message from {} to {}", chatMessageDto.getFromUser(), chatMessageDto.getToUser());
        ChatMessageDto message = messageService.saveMessage(chatMessageDto);
        messageService.sendToRecipient(message);
    }
}
