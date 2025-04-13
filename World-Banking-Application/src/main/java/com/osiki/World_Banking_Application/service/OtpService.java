package com.osiki.World_Banking_Application.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    public String generateOtp(String email){
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        return otp;
    }

    public boolean validateOtp(String email, String otp){
        String storedOtp = otpStorage.get(email);

        return otp != null && otp.trim().equals(storedOtp);
    }

    public void clearOtp(String email){
        otpStorage.remove(email);

    }
}
