package com.osiki.World_Banking_Application.service.impl;

import com.osiki.World_Banking_Application.domain.entity.UserEntity;
import com.osiki.World_Banking_Application.service.EmailService;
import com.osiki.World_Banking_Application.service.NotificationService;
import com.osiki.World_Banking_Application.utils.UtilityClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.osiki.World_Banking_Application.utils.UtilityClass.getCurrentIpAddress;
import static com.osiki.World_Banking_Application.utils.UtilityClass.getDeviceDetails;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;


    @Override
    public void sendLoginNotificationEmail(UserEntity user, HttpServletRequest request) {

        String ipAddress = getCurrentIpAddress(request);
        String device = getDeviceDetails(request);
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ssa EEEE, MM dd, yyyy 'GMT'"));

        String emailContent = String.format(
                """
                        Hello %s,
                        
                        Welcome Back!
                        
                        We noticed a new sign-in to your account using %s from %s at %s.
                        
                        If you signed in recently, no need to worry, and you can disregard this message.
                        
                        If that wasn't you or you don't recognized this sign-in, we strongly recommend that you change your password as soon as possible and do not hesitate to contact us if you need further assistance.
                        
                        Why should you contact us? We take your security seriously and we want to ensure every activities on your account is perform by you.
                        
                        The Support Team""",

                user.getFirstName(), device, ipAddress, timeStamp
        );

        try{
            emailService.sendSimpleEmail(user.getEmail(), "New Sign-In to Your World Banking Account",
                    emailContent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send login notification email");
        }

    }
}
