package com.casumo.races.controller;

import com.casumo.races.dto.AuthResponse;
import com.casumo.races.dto.LoginDto;
import com.casumo.races.dto.RegisterDto;
import com.casumo.races.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginDto authentication) {
        return authService.login(authentication);
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterDto registration) {
        return authService.register(registration);
    }
}

