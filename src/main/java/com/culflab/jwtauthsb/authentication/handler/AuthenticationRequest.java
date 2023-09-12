package com.culflab.jwtauthsb.authentication.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    private String account;
    private String password;
}
