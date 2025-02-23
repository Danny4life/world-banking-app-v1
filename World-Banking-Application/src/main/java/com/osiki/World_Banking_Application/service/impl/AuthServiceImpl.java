package com.osiki.World_Banking_Application.service.impl;

import com.osiki.World_Banking_Application.domain.entity.UserEntity;
import com.osiki.World_Banking_Application.domain.enums.Role;
import com.osiki.World_Banking_Application.payload.request.EmailDetails;
import com.osiki.World_Banking_Application.payload.request.UserRequest;
import com.osiki.World_Banking_Application.payload.response.AccountInfo;
import com.osiki.World_Banking_Application.payload.response.BankResponse;
import com.osiki.World_Banking_Application.repository.UserRepository;
import com.osiki.World_Banking_Application.service.AuthService;
import com.osiki.World_Banking_Application.service.EmailService;
import com.osiki.World_Banking_Application.utils.AccountUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.osiki.World_Banking_Application.utils.AccountUtil.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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


        // TODO --- abstract it to another class
        String emailContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                " body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
                " .email-container { background-color: #ffffff; padding:20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }" +
                " h2 { color: #2c3450; }" +
                " p { font-size: 16px; color: #333; }" +
                " .account-details { font-weight: bold; color: #3498db; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='email-container'>" +
                " <h2>CONGRATULATIONS!!! Your Account Has Been Successfully Created</h2>" +
                " <p>Dear " + savedUser.getFirstName() + " " + savedUser.getLastName() + ",</p>" +
                " <p>Your account has been successfully created. Below are your account details:</p>" +
                " <p class='account-details'>Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "</p>" +
                " <p class='account-details'>Account Number: " + savedUser.getAccountNumber() + "</p>" +
                " <p>Best Regards,<br>Bank Support Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody(emailContent)
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
}
