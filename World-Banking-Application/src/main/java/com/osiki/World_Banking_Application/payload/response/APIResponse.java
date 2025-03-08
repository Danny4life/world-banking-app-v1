package com.osiki.World_Banking_Application.payload.response;

import com.osiki.World_Banking_Application.utils.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class APIResponse<T>{

    private String message;

    private T data;

    private String responseTime;


    public APIResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.responseTime = DateUtils.dateToString(LocalDateTime.now());
    }

    public APIResponse(String message) {
        this.message = message;
        this.responseTime = DateUtils.dateToString(LocalDateTime.now());
    }
}
