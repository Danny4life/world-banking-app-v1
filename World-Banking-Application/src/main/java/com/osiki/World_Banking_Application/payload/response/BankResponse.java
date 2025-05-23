package com.osiki.World_Banking_Application.payload.response;

import com.osiki.World_Banking_Application.domain.entity.ErrorDetails;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class BankResponse<T>{

    private String responseCode;

    private String responseMessage;

    private AccountInfo accountInfo;

    public BankResponse(String message) {
        this.responseMessage = message;
    }

    public BankResponse(String responseCode, String responseMessage, AccountInfo accountInfo) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.accountInfo = accountInfo;
    }

    public BankResponse(String uploadedSuccessfully, String fileUrl) {
    }

    public BankResponse(String message, ErrorDetails errorDetails) {
    }
}
