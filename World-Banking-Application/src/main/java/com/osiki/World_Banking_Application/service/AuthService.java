package com.osiki.World_Banking_Application.service;

import com.osiki.World_Banking_Application.payload.request.AdminRequest;
import com.osiki.World_Banking_Application.payload.request.LoginRequest;
import com.osiki.World_Banking_Application.payload.request.UserRequest;
import com.osiki.World_Banking_Application.payload.response.APIResponse;
import com.osiki.World_Banking_Application.payload.response.AdminSignupResponse;
import com.osiki.World_Banking_Application.payload.response.BankResponse;
import com.osiki.World_Banking_Application.payload.response.JwtAuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    BankResponse registerUser(UserRequest userRequest);

//    BankResponse registerAdmin(UserRequest userRequest);


    ResponseEntity<APIResponse<JwtAuthResponse>> login(LoginRequest loginRequest);

    ResponseEntity<APIResponse<AdminSignupResponse>> registerAdmin(AdminRequest adminRequest);
}
