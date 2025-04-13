package com.osiki.World_Banking_Application.domain.entity;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {

    private String message;

    private HttpStatus status;

    private String debugMessage;

    private LocalDateTime dateTime;

    private String details;
}
