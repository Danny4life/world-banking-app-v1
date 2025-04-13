package com.osiki.World_Banking_Application.infrastructure.controller;

import com.osiki.World_Banking_Application.infrastructure.security.JwtAuthenticationFilter;
import com.osiki.World_Banking_Application.infrastructure.security.JwtTokenProvider;
import com.osiki.World_Banking_Application.payload.response.BankResponse;
import com.osiki.World_Banking_Application.service.GeneralUserService;
import com.osiki.World_Banking_Application.utils.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class GeneralUserController {

    private final GeneralUserService generalUserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletRequest request;
    private final JwtAuthenticationFilter authenticationFilter;

    @PutMapping("profile-picture")
    public ResponseEntity<BankResponse<String>> profilePicsUpload(@RequestParam MultipartFile profilePic){

        if(profilePic.getSize() > AppConstant.MAX_FILE_SIZE){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BankResponse<>("File size exceed the normal limit"));
        }

        return generalUserService.uploadProfilePics(profilePic);

    }
}
