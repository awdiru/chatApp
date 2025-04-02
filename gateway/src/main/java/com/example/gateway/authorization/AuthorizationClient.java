package com.example.gateway.authorization;

import com.example.gateway.cash.CashService;
import com.example.gateway.client.BaseClient;
import com.example.template.model.user.dto.model.authentication.AuthenticationRequest;
import com.example.template.model.user.User;
import com.example.template.model.user.dto.model.authentication.AuthenticationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Objects;

@Service
public class AuthorizationClient extends BaseClient {
    private static final String API_PREFIX = "/auth";
    private final CashService cashService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AuthorizationClient(@Value("${chatApp-authorization.url}") String serverUrl,
                               RestTemplateBuilder builder,
                               CashService cashService) {

        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.cashService = cashService;
    }

    public ResponseEntity<?> createAuthenticationToken (AuthenticationRequest authenticationRequest) {
        try {
            ResponseEntity<Object> response = post("/login", authenticationRequest);
            String json = objectMapper.writeValueAsString(response.getBody());

            AuthenticationResponse authenticationResponse = objectMapper.readValue(
                    json, AuthenticationResponse.class);

            cashService.addToCash(authenticationResponse.getJwt(), authenticationRequest.getUsername());

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> registerUser (User user) {
        return post("/signup", user);
    }

    public ResponseEntity<?> validateToken (String authToken) {
        if (cashService.hasKey(authToken))
            return ResponseEntity.ok("");

        return post("/validate", authToken);
    }
}
