package com.osiki.World_Banking_Application.infrastructure.controller;

import com.osiki.World_Banking_Application.payload.request.AdminRequest;
import com.osiki.World_Banking_Application.payload.request.LoginRequest;
import com.osiki.World_Banking_Application.payload.request.UserRequest;
import com.osiki.World_Banking_Application.payload.response.APIResponse;
import com.osiki.World_Banking_Application.payload.response.AdminSignupResponse;
import com.osiki.World_Banking_Application.payload.response.BankResponse;
import com.osiki.World_Banking_Application.payload.response.JwtAuthResponse;
import com.osiki.World_Banking_Application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PostExchange;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    public BankResponse<?> createAccount(@RequestBody UserRequest userRequest){
        return authService.registerUser(userRequest);

    }

    @PostMapping("admin/register")
    public ResponseEntity<APIResponse<AdminSignupResponse>> registerAdmin(@RequestBody AdminRequest adminRequest){
        return authService.registerAdmin(adminRequest);

    }

    @PostMapping("login")
    public ResponseEntity<APIResponse<JwtAuthResponse>> login(@RequestBody LoginRequest loginRequest){

        return authService.login(loginRequest);

    }
}
