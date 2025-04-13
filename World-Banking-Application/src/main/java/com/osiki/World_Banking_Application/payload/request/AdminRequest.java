package com.osiki.World_Banking_Application.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;
}
