package com.example.authorization.service;

import com.example.template.model.user.User;
import com.example.authorization.repository.UserRepository;
import com.example.template.model.user.dto.model.authentication.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}
