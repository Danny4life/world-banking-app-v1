package com.osiki.World_Banking_Application.infrastructure.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException{

    private final String customMessage;

    public ApplicationException(String customMessage) {
        this.customMessage = customMessage;
    }
}
