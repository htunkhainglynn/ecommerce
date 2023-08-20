package com.project.ecommerce.service;

import com.project.ecommerce.entitiy.Notification;

import java.util.List;

public interface NotificationService {

    public List<Notification> getNotificationByUser();

    void saveNotification(Notification notification);
}
