package com.osiki.World_Banking_Application.infrastructure.controller;

import com.osiki.World_Banking_Application.payload.request.CreditAndDebitRequest;
import com.osiki.World_Banking_Application.payload.request.TransferRequest;
import com.osiki.World_Banking_Application.payload.response.BankResponse;
import com.osiki.World_Banking_Application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/credit-account")
    public BankResponse<?> creditAccount(@RequestBody CreditAndDebitRequest request){

        return userService.creditAccount(request);

    }

    @PostMapping("/debit-account")
    public BankResponse<?> debitAccount(@RequestBody CreditAndDebitRequest request){

        return userService.debitAccount(request);

    }

    @PostMapping("/transfer")
    public BankResponse<?> transfer(@RequestBody TransferRequest request){

        return userService.transfer(request);

    }

    @PostMapping("/verify-otp-and-credit")
    public BankResponse<?> verifyOTPAndCompleteTransfer(@RequestBody TransferRequest request,
                                                        @RequestParam String otp){

        return userService.verifyOtpAndCompleteTransfer(request, otp);

    }
}
