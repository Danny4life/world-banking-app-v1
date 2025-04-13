package com.osiki.World_Banking_Application.infrastructure.exception;

public class EmailNotSendException extends RuntimeException{

    public EmailNotSendException(String message) {
        super(message);
    }
}
