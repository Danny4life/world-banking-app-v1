package com.osiki.World_Banking_Application.infrastructure.controller;

import com.osiki.World_Banking_Application.payload.request.UserRequest;
import com.osiki.World_Banking_Application.payload.response.BankResponse;
import com.osiki.World_Banking_Application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    public BankResponse<?> createAccount(@RequestBody UserRequest userRequest){
        return authService.registerUser(userRequest);

    }
}
