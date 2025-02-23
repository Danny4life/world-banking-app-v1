package com.osiki.World_Banking_Application.service;

import com.osiki.World_Banking_Application.payload.request.UserRequest;
import com.osiki.World_Banking_Application.payload.response.BankResponse;

public interface AuthService {

    BankResponse registerUser(UserRequest userRequest);
}
