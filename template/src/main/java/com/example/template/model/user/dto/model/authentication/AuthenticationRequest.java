package com.example.template.model.user.dto.model.authentication;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AuthenticationRequest {
    private String username;
    private String password;
}
