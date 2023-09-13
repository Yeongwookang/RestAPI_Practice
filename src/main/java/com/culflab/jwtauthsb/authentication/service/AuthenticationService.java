package com.culflab.jwtauthsb.authentication.service;

import com.culflab.jwtauthsb.authentication.handler.AuthenticationRequest;
import com.culflab.jwtauthsb.authentication.handler.AuthenticationResponse;
import com.culflab.jwtauthsb.authentication.handler.RegisterRequest;
import com.culflab.jwtauthsb.member.repository.MemberRepository;

import com.culflab.jwtauthsb.member.service.handler.Member;
import com.culflab.jwtauthsb.member.service.handler.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = Member.builder()
                .name(request.getName())
                .account(request.getAccount())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(request.getCreatedAt())
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getAccount(),
                        request.getPassword()
                )
        );
        var user = repository.findByAccount(request.getAccount())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
