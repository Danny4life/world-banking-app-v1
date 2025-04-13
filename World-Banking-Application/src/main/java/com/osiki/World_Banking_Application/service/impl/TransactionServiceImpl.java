package com.osiki.World_Banking_Application.service.impl;

import com.osiki.World_Banking_Application.domain.entity.Transaction;
import com.osiki.World_Banking_Application.payload.request.TransactionRequest;
import com.osiki.World_Banking_Application.repository.TransactionRepository;
import com.osiki.World_Banking_Application.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;


    @Override
    public void saveTransaction(TransactionRequest request) {

        Transaction transaction = Transaction.builder()
                .transactionType(request.getTransactionType())
                .accountNumber(request.getAccountNumber())
                .amount(request.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);

        System.out.println("Transaction saved successfully");

    }
}
