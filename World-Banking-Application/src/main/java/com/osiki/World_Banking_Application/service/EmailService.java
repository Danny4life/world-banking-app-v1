package com.osiki.World_Banking_Application.service;

import com.osiki.World_Banking_Application.payload.request.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);


    void sendSimpleEmail(String to, String subject, String message);

    void sendEmailWithAttachment(EmailDetails emailDetails);
}
