package com.osiki.World_Banking_Application.service;

import com.osiki.World_Banking_Application.payload.response.BankResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface GeneralUserService {

    ResponseEntity<BankResponse<String>> uploadProfilePics(MultipartFile profilePics);
}
