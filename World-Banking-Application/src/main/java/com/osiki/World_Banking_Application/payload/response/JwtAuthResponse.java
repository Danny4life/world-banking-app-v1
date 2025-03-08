package com.osiki.World_Banking_Application.payload.response;

import com.osiki.World_Banking_Application.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String profilePicture;

    private String email;

    private String gender;

    private Role role;

    private String accessToken;

    private String tokenType = "Bearer";
}
