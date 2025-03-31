package com.example.gateway.authorization;

import com.example.template.model.user.dto.model.authentication.AuthenticationRequest;
import com.example.template.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatApp/auth")
@Slf4j
public class AuthorizationController {
    @Autowired
    private AuthorizationClient client;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken (@RequestBody AuthenticationRequest authenticationRequest) {
        log.debug("POST createAuthenticationToken /auth/login");
        return client.createAuthenticationToken(authenticationRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser (@RequestBody User user) {
        log.debug("GET registerUser /auth/signup");
        return client.registerUser(user);
    }
}
