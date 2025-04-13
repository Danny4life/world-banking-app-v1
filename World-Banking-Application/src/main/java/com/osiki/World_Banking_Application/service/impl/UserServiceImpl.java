package com.osiki.World_Banking_Application.service.impl;

import com.osiki.World_Banking_Application.domain.entity.UserEntity;
import com.osiki.World_Banking_Application.payload.request.CreditAndDebitRequest;
import com.osiki.World_Banking_Application.payload.request.EmailDetails;
import com.osiki.World_Banking_Application.payload.request.TransactionRequest;
import com.osiki.World_Banking_Application.payload.request.TransferRequest;
import com.osiki.World_Banking_Application.payload.response.AccountInfo;
import com.osiki.World_Banking_Application.payload.response.BankResponse;
import com.osiki.World_Banking_Application.repository.UserRepository;
import com.osiki.World_Banking_Application.service.EmailService;
import com.osiki.World_Banking_Application.service.OtpService;
import com.osiki.World_Banking_Application.service.TransactionService;
import com.osiki.World_Banking_Application.service.UserService;
import com.osiki.World_Banking_Application.utils.AccountUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.osiki.World_Banking_Application.utils.AccountUtil.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OtpService otpService;
    private final TransactionService transactionService;

    @Override
    public BankResponse creditAccount(CreditAndDebitRequest request) {

        // to credit an account, first check if the account exists
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());


        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // TODO -- create a custom exception for our banking application

        UserEntity userToCredit = userRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(()-> new RuntimeException("Account number not found"));

        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));

        userRepository.save(userToCredit);

        // TODO -- have you integrate with Twilio API before? -- is used for phone alert

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(userToCredit.getEmail())
                .messageBody("Your account has been credited with " +request.getAmount() +
                        " from " + userToCredit.getFirstName() + " Your current account balance is " +
                        userToCredit.getAccountBalance())
                .build();

        emailService.sendEmailAlert(creditAlert);

        // TODO -- SMS alert will come in here


        TransactionRequest transactionRequest = TransactionRequest.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionRequest);

        return BankResponse.builder()
                .responseCode(ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditAndDebitRequest request) {

        // to debit an account, first check if the account exists, then also check for
        // insufficient fund

        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());

        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();

        }

        UserEntity userToDebit = userRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(()-> new RuntimeException("Account number not found"));


        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();

        if(availableBalance.intValue() < debitAmount.intValue()){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_BALANCE_CODE)
                    .responseMessage(ACCOUNT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("DEBIT ALERT")
                    .recipient(userToDebit.getEmail())
                    .messageBody("The sum of " + request.getAmount() +
                            " has been deducted from your account! Your current account balance is " +
                            userToDebit.getAccountBalance())
                    .build();

            emailService.sendEmailAlert(debitAlert);

            // TODO -- SEND SMS ALERT
            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(request.getAmount())
                    .build();

            transactionService.saveTransaction(transactionRequest);
        }

        return BankResponse.builder()
                .responseCode(ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage(ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToDebit.getFirstName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();
    }


    @Transactional
    @Override
    public BankResponse transfer(TransferRequest request) {

        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

        if(!isDestinationAccountExists){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(()-> new RuntimeException("Account number not found"));

        if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_BALANCE_CODE)
                    .responseMessage(ACCOUNT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }


        BigDecimal threshHoldAmount = new BigDecimal("500000");

        if(request.getAmount().compareTo(threshHoldAmount) >= 0){
            String otp = otpService.generateOtp(sourceAccountUser.getEmail());

            EmailDetails otpEmail = EmailDetails.builder()
                    .subject("OTP for Transfer Verification")
                    .recipient(sourceAccountUser.getEmail())
                    .messageBody("Your OTP for the transfer of " + request.getAmount() + " is " + otp)
                    .build();

            emailService.sendEmailAlert(otpEmail);

            return BankResponse.builder()
                    .responseCode("009")
                    .responseMessage("OTP IS REQUIRED: " + otp)
                    .accountInfo(null)
                    .build();

        }
        return completeTransfer(sourceAccountUser, request);
    }





    private BankResponse completeTransfer(UserEntity sourceAccountUser, TransferRequest request) {
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);

        String sourceUsername = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName();

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of " + request.getAmount() +
                        " has been deducted from your account! Your current account balance is " +
                        sourceAccountUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(debitAlert);

        // TODO -- SEND SMS ALERT

        // TODO -- IMPLEMENT TRANSACTION FEATURE
        TransactionRequest debitTransactionRequest = TransactionRequest.builder()
                .accountNumber(sourceAccountUser.getAccountNumber())
                .transactionType("DEBIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(debitTransactionRequest);

        UserEntity destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(()-> new RuntimeException("Account number not found"));

        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("Your account has been credited with " + request.getAmount() + " from " +
                        sourceUsername + " your current account balance is " + destinationAccountUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(creditAlert);

        // TODO -- SEND SMS ALERT


        TransactionRequest creditTransactionRequest = TransactionRequest.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(creditTransactionRequest);

        return BankResponse.builder()
                .responseCode(TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();

    }



    @Override
    public BankResponse verifyOtpAndCompleteTransfer(TransferRequest request, String otp) {

        UserEntity sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(()-> new RuntimeException("Account number not found"));

        boolean isOtpValid = otpService.validateOtp(sourceAccountUser.getEmail(), otp);

        if(!isOtpValid){
            return BankResponse.builder()
                    .responseCode("011")
                    .responseMessage("INVALID OTP")
                    .accountInfo(null)
                    .build();

        }

        return completeTransfer(sourceAccountUser, request);
    }
}
