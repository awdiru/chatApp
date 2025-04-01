package com.example.authorization.controller;

import com.example.authorization.util.JwtUtil;
import com.example.template.model.user.User;
import com.example.template.model.user.dto.model.authentication.AuthenticationRequest;
import com.example.template.model.user.dto.model.authentication.AuthenticationResponse;
import com.example.authorization.service.JwtService;
import com.example.authorization.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {

        final String jwt = jwtService.createJwtToken(authenticationRequest);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws Exception {

        if (userService.findByUserName(user.getUserName()) != null)
            throw new RuntimeException("Username is already taken");

        userService.save(user);

        AuthenticationRequest authenticationRequest =
                new AuthenticationRequest(user.getUserName(), user.getPassword());
        final String jwt = jwtService.createJwtToken(authenticationRequest);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("X-jwt") String authHeader) {
        try {
            String token = authHeader.substring(7);
            jwtUtil.validateToken(token);
            return ResponseEntity.ok().body("");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        }
    }
}
