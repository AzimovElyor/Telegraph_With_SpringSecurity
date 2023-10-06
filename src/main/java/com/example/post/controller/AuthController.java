package com.example.post.controller;

import com.example.post.dto.JwtResponse;
import com.example.post.dto.LoginDto;
import com.example.post.dto.RegisterDto;
import com.example.post.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> signUp(@RequestBody RegisterDto registerDto){
        System.out.println("registerDto = " + registerDto);
      return new ResponseEntity<>(authService.register(registerDto), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> signIn(
            @RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authService.login(loginDto));
    }
}
