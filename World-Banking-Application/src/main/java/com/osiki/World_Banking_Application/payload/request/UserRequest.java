package com.osiki.World_Banking_Application.payload.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {


    private String firstName;

    private String lastName;

    private String otherName;

    private String gender;

    private String address;

    private String email;

    private String password;

    private String phoneNumber;
}
