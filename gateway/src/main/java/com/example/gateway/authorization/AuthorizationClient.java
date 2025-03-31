package com.example.gateway.authorization;

import com.example.gateway.client.BaseClient;
import com.example.template.model.user.dto.model.authentication.AuthenticationRequest;
import com.example.template.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class AuthorizationClient extends BaseClient {
    private static final String API_PREFIX = "/auth";

    @Autowired
    public AuthorizationClient(@Value("${chatApp-authorization.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<?> createAuthenticationToken (AuthenticationRequest authenticationRequest) {
        return post("/login", authenticationRequest);
    }

    public ResponseEntity<?> registerUser (User user) {
        return post("/signup", user);
    }

    public ResponseEntity<?> validateToken (String authToken) {
        return post("/validate", authToken);
    }
}
