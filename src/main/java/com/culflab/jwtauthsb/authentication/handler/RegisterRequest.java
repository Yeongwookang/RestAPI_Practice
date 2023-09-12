package com.culflab.jwtauthsb.authentication.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String account;
    private String password;
    private String name;
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt(){
        return LocalDateTime.now();
    }
}
