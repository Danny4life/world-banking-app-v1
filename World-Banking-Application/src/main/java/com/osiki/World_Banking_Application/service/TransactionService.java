package com.osiki.World_Banking_Application.service;

import com.osiki.World_Banking_Application.payload.request.TransactionRequest;

public interface TransactionService {

    void saveTransaction(TransactionRequest request);
}
