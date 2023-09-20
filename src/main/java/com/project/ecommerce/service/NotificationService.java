package com.project.ecommerce.service;

import com.project.ecommerce.entitiy.Notification;

import java.util.List;

public interface NotificationService {

    void saveNotification(Notification notification);

    List<Notification> getNotificationByUsername(String username);
}
