package com.culflab.jwtauthsb.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @PostMapping("sign-in")
    public ResponseEntity<String> signIn(){
        return ResponseEntity.ok().body("token");
    }
    @PostMapping("sign-up")
    public ResponseEntity<String> signUp(){
        return ResponseEntity.ok().body("token");
    }
}
