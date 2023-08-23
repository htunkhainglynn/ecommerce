package com.project.ecommerce.service.implementation;

import com.project.ecommerce.entitiy.Notification;
import com.project.ecommerce.repo.NotificationRepo;
import com.project.ecommerce.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;

    @Autowired
    public NotificationServiceImpl(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public void saveNotification(Notification notification) {
        String username = getUsername();
        notification.setUsername(username);
        notificationRepo.save(notification);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Notification> getNotificationByUserName(String username) {
        return notificationRepo.findByUsername(username);
    }
}
