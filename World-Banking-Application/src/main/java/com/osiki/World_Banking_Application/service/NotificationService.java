package com.osiki.World_Banking_Application.service;

import com.osiki.World_Banking_Application.domain.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;

public interface NotificationService {

    void sendLoginNotificationEmail(UserEntity user, HttpServletRequest request);
}
