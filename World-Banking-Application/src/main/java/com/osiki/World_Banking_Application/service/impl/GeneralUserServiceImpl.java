package com.osiki.World_Banking_Application.service.impl;

import com.osiki.World_Banking_Application.domain.entity.UserEntity;
import com.osiki.World_Banking_Application.infrastructure.security.JwtAuthenticationFilter;
import com.osiki.World_Banking_Application.infrastructure.security.JwtTokenProvider;
import com.osiki.World_Banking_Application.payload.response.BankResponse;
import com.osiki.World_Banking_Application.repository.UserRepository;
import com.osiki.World_Banking_Application.service.GeneralUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneralUserServiceImpl implements GeneralUserService {

    private final FileUploadServiceImpl fileUploadService;
    private final UserRepository userRepository;
    private final HttpServletRequest request;
    private final JwtAuthenticationFilter authenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public ResponseEntity<BankResponse<String>> uploadProfilePics(MultipartFile profilePics) {

        String token = authenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);

        String fileUrl = null;


        try{
            if(userEntityOptional.isPresent()){
                fileUrl = fileUploadService.uploadFile(profilePics);

                UserEntity userEntity = userEntityOptional.get();
                userEntity.setProfilePicture(fileUrl);

                userRepository.save(userEntity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(
                new BankResponse<>(
                        "Uploaded Successfully",
                        fileUrl
                )
        );
    }
}
