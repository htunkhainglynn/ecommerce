package com.project.ecommerce.service.implementation;

import com.project.ecommerce.entitiy.Notification;
import com.project.ecommerce.repo.NotificationRepo;
import com.project.ecommerce.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public List<Notification> getNotificationByUser() {
        String username = getUsername();
        return notificationRepo.findByUsername(username);
    }

    @Override
    public void saveNotification(Notification notification) {
        String username = getUsername();
        notification.setUsername(username);
        notificationRepo.save(notification);
    }
}
