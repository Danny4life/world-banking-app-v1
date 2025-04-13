package com.osiki.World_Banking_Application.infrastructure.exception;

import com.osiki.World_Banking_Application.domain.entity.ErrorDetails;
import com.osiki.World_Banking_Application.payload.response.BankResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(EmailNotSendException.class)
    public ResponseEntity<BankResponse<ErrorDetails>> handleEmailNotSendException(final EmailNotSendException ex){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .debugMessage("Email Not Send")
                .build();

        BankResponse<ErrorDetails> response = new BankResponse<>(ex.getMessage(), errorDetails);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorDetails> handleApplicationException(final ApplicationException ex){

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDateTime(LocalDateTime.now());
        errorDetails.setDetails(String.valueOf(HttpStatus.BAD_REQUEST));
        errorDetails.setMessage(errorDetails.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }
}
