package com.example.client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import com.example.template.model.message.dto.model.ChatMessageDto;
import com.example.template.model.user.dto.model.authentication.AuthenticationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import static com.example.template.constants.Constants.AUTH_HEADER;

public class ChatClient {
    private static final String HTTP_URL = "${chatApp-gateway.url}";
    private static final String WS_URL = "ws://localhost:8080/ws";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String jwtToken;
    private StompSession stompSession;
    private final Consumer<String> messageConsumer;

    public ChatClient(Consumer<String> messageConsumer) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.messageConsumer = messageConsumer;
    }

    public boolean login(String name, String password, String path) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(new AuthenticationRequest(name, password));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_URL + "/auth" + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            this.jwtToken = "Bearer " + objectMapper.readTree(response.body()).get("token").asText();
            return true;
        }
        return false;
    }

    public void sendMessage(String message,String fromUser ,String toUser) throws IOException, InterruptedException {
        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .message(message)
                .toUser(toUser)
                .fromUser(getCurrentUsername())
                .time(LocalDateTime.now())
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_URL + "/chatApp/send"))
                .header("Content-Type", "application/json")
                .header(AUTH_HEADER, jwtToken)
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(chatMessageDto)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200)
            throw new IOException("Message sending failed: " + response.body());

    }

    public void startClient() throws ExecutionException, InterruptedException, JsonProcessingException {
        WebSocketStompClient stompClient = new WebSocketStompClient(
                new SockJsClient(Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient())))
        );
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String username = getCurrentUsername();
        this.stompSession = stompClient.connectAsync(WS_URL, new StompSessionHandlerAdapter() {
                })
                .get();

        // Подписка на персональную очередь
        stompSession.subscribe("/topic/user/" + username, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                ChatMessageDto message = (ChatMessageDto) payload;
                messageConsumer.accept(
                        String.format("[%s] %s: %s",
                                message.getTime(),
                                message.getFromUser(),
                                message.getMessage())
                );
            }
        });
    }

    private String getCurrentUsername() throws JsonProcessingException {
        // Декодирование JWT для получения имени пользователя
        String[] chunks = jwtToken.split("\\.");
        String payload = new String(java.util.Base64.getDecoder().decode(chunks[1]));
        return objectMapper.readTree(payload).get("sub").asText();
    }
}