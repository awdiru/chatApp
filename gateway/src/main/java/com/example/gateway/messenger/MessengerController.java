package com.example.gateway.messenger;

import com.example.gateway.authorization.AuthorizationClient;
import com.example.gateway.cash.CashService;
import com.example.template.model.exception.ExceptionResponse;
import com.example.template.model.message.dto.model.ChatMessageDto;
import com.example.template.model.user.dto.model.authentication.AuthenticationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.template.constants.Constants.AUTH_HEADER;

@RestController
@RequestMapping("/chatApp")
@Slf4j
public class MessengerController {
    @Autowired
    private AuthorizationClient authorizationClient;
    @Autowired
    private MessengerClient messengerClient;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestHeader(AUTH_HEADER) String authHeader,
                                         @RequestBody ChatMessageDto chatMessageDto) {

        log.info("send message authHeader: {}", authHeader);
        ResponseEntity<?> valid = authorizationClient.validateToken(authHeader);
        if (!valid.getStatusCode().is2xxSuccessful())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(),
                            "The user is not logged in",
                            "/chatApp/send"));

        messengerClient.sendMessage(chatMessageDto);
        return ResponseEntity.ok("Message sent");
    }

    @PostMapping
    public ResponseEntity<?> createAuthenticationToken (@RequestHeader(AUTH_HEADER) String authHeader,
                                                        @RequestBody AuthenticationRequest authenticationRequest) {
        log.debug("POST createAuthenticationToken /auth/login");
        return authorizationClient.createAuthenticationToken(authenticationRequest);
    }
}
