package com.osiki.World_Banking_Application.service.impl;

import com.osiki.World_Banking_Application.domain.entity.Admin;
import com.osiki.World_Banking_Application.domain.entity.UserEntity;
import com.osiki.World_Banking_Application.domain.enums.Role;
import com.osiki.World_Banking_Application.infrastructure.exception.ApplicationException;
import com.osiki.World_Banking_Application.infrastructure.security.JwtTokenProvider;
import com.osiki.World_Banking_Application.payload.request.AdminRequest;
import com.osiki.World_Banking_Application.payload.request.EmailDetails;
import com.osiki.World_Banking_Application.payload.request.LoginRequest;
import com.osiki.World_Banking_Application.payload.request.UserRequest;
import com.osiki.World_Banking_Application.payload.response.*;
import com.osiki.World_Banking_Application.repository.AdminRepository;
import com.osiki.World_Banking_Application.repository.UserRepository;
import com.osiki.World_Banking_Application.service.AuthService;
import com.osiki.World_Banking_Application.service.EmailService;
import com.osiki.World_Banking_Application.service.NotificationService;
import com.osiki.World_Banking_Application.utils.AccountUtil;
import com.osiki.World_Banking_Application.utils.UtilityClass;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.osiki.World_Banking_Application.utils.AccountUtil.*;
import static com.osiki.World_Banking_Application.utils.UtilityClass.emailContent;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletRequest request;
    private final NotificationService notificationService;

    private final ModelMapper modelMapper;

    private final AdminRepository adminRepository;

    @Transactional
    @Override
    public BankResponse registerUser(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail())){
            BankResponse response = BankResponse.builder()
                    .responseCode(ACCOUNT_EXISTS_CODE)
                    .responseMessage(ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();

            return response;
        }

        UserEntity newUser = UserEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .gender(userRequest.getGender())
                .phoneNumber(userRequest.getPhoneNumber())
                .accountNumber(AccountUtil.generateAccountNumber())
                .address(userRequest.getAddress())
                .accountBalance(BigDecimal.ZERO)
                .profilePicture("https://res.cloudinary.com/dpfqbb9pl/image/upload/v1701260428/maleprofile_ffeep9.png")
                .accountStatus("ACTIVE")
                .role(Role.ROLE_USER)
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody(emailContent(savedUser))
                .build();

        emailService.sendEmailAlert(emailDetails);


        return BankResponse.builder()
                .responseCode(ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .build())
                .build();
    }

//    @Override
//    public BankResponse registerAdmin(UserRequest userRequest) {
//        if(userRepository.existsByEmail(userRequest.getEmail())){
//            BankResponse response = BankResponse.builder()
//                    .responseCode(ACCOUNT_EXISTS_CODE)
//                    .responseMessage(ACCOUNT_EXISTS_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//
//            return response;
//        }
//
//        UserEntity newAdmin = UserEntity.builder()
//                .firstName(userRequest.getFirstName())
//                .lastName(userRequest.getLastName())
//                .otherName(userRequest.getOtherName())
//                .email(userRequest.getEmail())
//                .password(passwordEncoder.encode(userRequest.getPassword()))
//                .gender(userRequest.getGender())
//                .phoneNumber(userRequest.getPhoneNumber())
//                .address(userRequest.getAddress())
//                .accountStatus("ACTIVE")
//                .role(Role.ROLE_ADMIN)
//                .build();
//
//        UserEntity savedAdmin = userRepository.save(newAdmin);
//
//        EmailDetails emailDetails = EmailDetails.builder()
//                .recipient(savedAdmin.getEmail())
//                .subject("ACCOUNT CREATION")
//                .messageBody(emailContent(savedAdmin))
//                .build();
//
//        emailService.sendEmailAlert(emailDetails);
//
//
//        return BankResponse.builder()
//                .responseCode(ACCOUNT_CREATION_SUCCESS_CODE)
//                .responseMessage(ACCOUNT_CREATION_SUCCESS_MESSAGE)
//                .accountInfo(null)
//                .build();
//    }

    @Override
    public ResponseEntity<APIResponse<JwtAuthResponse>> login(LoginRequest loginRequest) {

        String emailOrPhone = loginRequest.getEmailOrPhone();

        Optional<UserEntity> userEntityOptional = isEmail(emailOrPhone)
                ? userRepository.findByEmail(emailOrPhone)
                : userRepository.findByPhoneNumber(emailOrPhone);

        Optional<Admin> adminOptional = isEmail(emailOrPhone)
                ? adminRepository.findByEmail(emailOrPhone)
                : adminRepository.findByPhoneNumber(emailOrPhone);



        if(userEntityOptional.isEmpty() && adminOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new APIResponse<>("Invalid email/phone number or password", null));

        }


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailOrPhone, loginRequest.getPassword())
        );


        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);




        if(userEntityOptional.isPresent()){
            UserEntity userEntity = userEntityOptional.get();
            notificationService.sendLoginNotificationEmail(userEntity, request);
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new APIResponse<>(
                                "Login Successful",
                                JwtAuthResponse.builder()
                                        .accessToken(token)
                                        .tokenType("Bearer")
                                        .id(userEntity.getId())
                                        .email(userEntity.getEmail())
                                        .gender(userEntity.getGender())
                                        .firstName(userEntity.getFirstName())
                                        .lastName(userEntity.getLastName())
                                        .profilePicture(userEntity.getProfilePicture())
                                        .role(userEntity.getRole())
                                        .build()
                        )
                );
        }else {
            Admin admin = adminOptional.get();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new APIResponse<>(
                                    "Login Successful",
                                    JwtAuthResponse.builder()
                                            .accessToken(token)
                                            .tokenType("Bearer")
                                            .id(admin.getId())
                                            .email(admin.getEmail())
                                            .firstName(admin.getFirstName())
                                            .lastName(admin.getLastName())
                                            .role(admin.getRole())
                                            .build()
                            )
                    );
        }
    }

    @Override
    public ResponseEntity<APIResponse<AdminSignupResponse>> registerAdmin(AdminRequest adminRequest) {

        boolean isPresent = adminRepository.existsByEmail(adminRequest.getEmail());

        if(isPresent){
            throw new ApplicationException("Admin with this email already exists");

        }

       Admin newAdmin = modelMapper.map(adminRequest, Admin.class);



        newAdmin.setRole(Role.ROLE_ADMIN);

        newAdmin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));

        Admin saveAdmin = adminRepository.save(newAdmin);

        AdminSignupResponse response =
                modelMapper.map(saveAdmin, AdminSignupResponse.class);



        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<AdminSignupResponse>("Account Created Successfully",
                        response));
    }








    private boolean isEmail(String input) {

        return input.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
