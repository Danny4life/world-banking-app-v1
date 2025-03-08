package com.osiki.World_Banking_Application.utils;

import com.osiki.World_Banking_Application.domain.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import ua_parser.Client;
import ua_parser.Parser;

public class UtilityClass {


    public static String getCurrentIpAddress(HttpServletRequest request){

        String ipAddress = request.getHeader("X-Forwarded-For");

        if(ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)){
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)){
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)){
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;

    }

    public static String getDeviceDetails(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        Parser parser = new Parser();
        Client client = parser.parse(userAgent);

        String browser = client.userAgent.family;
        String os = client.os.family;

        return browser + " on " + os;

    }

    public static String emailContent(UserEntity newUser){

        String emailContent;
        emailContent = "<!DOCTYPE html>" +
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
                " <p>Dear " + newUser.getFirstName() + " " + newUser.getLastName() + ",</p>" +
                " <p>Your account has been successfully created. Below are your account details:</p>" +
                " <p class='account-details'>Account Name: " + newUser.getFirstName() + " " + newUser.getLastName() + " " + newUser.getOtherName() + "</p>" +
                " <p class='account-details'>Account Number: " + newUser.getAccountNumber() + "</p>" +
                " <p>Best Regards,<br>Bank Support Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";


        return emailContent;

    }
}
