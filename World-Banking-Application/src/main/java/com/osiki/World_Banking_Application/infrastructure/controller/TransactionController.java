package com.osiki.World_Banking_Application.infrastructure.controller;

import com.itextpdf.text.DocumentException;
import com.osiki.World_Banking_Application.domain.entity.Transaction;
import com.osiki.World_Banking_Application.service.impl.BankStatementImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statement")
@RequiredArgsConstructor
public class TransactionController {

    private final BankStatementImpl bankStatement;

    @GetMapping
    public List<Transaction> generateStatement(@RequestParam String accountNumber,
                                               @RequestParam String startDate,
                                               @RequestParam String endDate)throws DocumentException, FileNotFoundException {



        return bankStatement.generateStatement(accountNumber, startDate, endDate);

    }
}
