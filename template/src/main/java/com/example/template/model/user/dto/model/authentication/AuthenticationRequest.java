package com.example.template.model.user.dto.model.authentication;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationRequest {
    private String username;
    private String password;
}
