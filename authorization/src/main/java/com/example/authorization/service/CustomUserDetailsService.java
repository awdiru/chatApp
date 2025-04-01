package com.example.authorization.service;

import com.example.authorization.repository.UserRepository;
import com.example.template.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + name));

        return new org.springframework.security.core.userdetails
                .User(user.getUserName(), user.getPassword(), new ArrayList<>());
    }
}
