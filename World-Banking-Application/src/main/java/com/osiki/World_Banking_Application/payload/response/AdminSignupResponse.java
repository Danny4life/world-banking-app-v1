package com.osiki.World_Banking_Application.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSignupResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;
}
