package com.example.gateway.messenger;

import com.example.gateway.authorization.AuthorizationClient;
import com.example.gateway.kafka.KafkaProducer;
import com.example.template.model.exception.ExceptionResponse;
import com.example.template.model.message.dto.model.ChatMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatApp/send")
public class MessengerController {
    private static final String HEADER_JWT = "X-jwt";
    @Autowired
    private AuthorizationClient authorizationClient;
    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping
    public ResponseEntity<?> sendMessage(@Header(HEADER_JWT) String authHeader,
                                         @RequestBody ChatMessageDto chatMessageDto) {

        ResponseEntity<?> valid = authorizationClient.validateToken(authHeader);
        if (!valid.getStatusCode().is2xxSuccessful())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ExceptionResponse(HttpStatus.UNAUTHORIZED, "The user is not logged in", "/chatApp/send"));

        kafkaProducer.sendMessage(chatMessageDto);
        return ResponseEntity.ok("Message sent");
    }
}
