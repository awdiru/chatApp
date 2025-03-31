package com.example.gateway.messenger;

import com.example.template.model.message.dto.model.ChatMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessengerClient {
    private static final String MESSAGE_TOPIC = "chat-messages";

    @Autowired
    private KafkaTemplate<String, ChatMessageDto> kafkaTemplate;

    public void sendMessage(ChatMessageDto message) {
        kafkaTemplate.send(MESSAGE_TOPIC, message);
    }
}
