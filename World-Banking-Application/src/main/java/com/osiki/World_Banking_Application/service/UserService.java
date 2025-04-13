package com.osiki.World_Banking_Application.service;

import com.osiki.World_Banking_Application.payload.request.CreditAndDebitRequest;
import com.osiki.World_Banking_Application.payload.request.TransferRequest;
import com.osiki.World_Banking_Application.payload.response.BankResponse;

public interface UserService {

    BankResponse creditAccount(CreditAndDebitRequest request);

    BankResponse debitAccount(CreditAndDebitRequest request);

    BankResponse transfer(TransferRequest request);

    BankResponse verifyOtpAndCompleteTransfer(TransferRequest request, String otp);

}
