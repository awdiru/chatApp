package com.example.authorization;

import com.example.authorization.annotation.AuthControllerExceptionHandler;
import com.example.authorization.util.JwtUtil;
import com.example.template.model.user.User;
import com.example.template.model.user.dto.model.authentication.AuthenticationRequest;
import com.example.template.model.user.dto.model.authentication.AuthenticationResponse;
import com.example.authorization.service.JwtService;
import com.example.authorization.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.template.constants.Constants.AUTH_HEADER;

@RestController
@RequestMapping("/auth")
@Slf4j
@AuthControllerExceptionHandler
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
        log.warn("Login user: {}", authenticationRequest.getUsername());
        final String jwt = jwtService.createJwtToken(authenticationRequest);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws Exception {

        log.warn("SignUp user: {}", user.getUsername());
        if (userService.findByUserName(user.getUsername()) != null)
            throw new RuntimeException("Username is already taken");

        user = userService.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(AUTH_HEADER) String authHeader) {

        log.warn("Validate authHeader: {}", authHeader);
        try {
            String token = authHeader.substring(7);
            jwtUtil.validateToken(token);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("body");
        }
    }
}
