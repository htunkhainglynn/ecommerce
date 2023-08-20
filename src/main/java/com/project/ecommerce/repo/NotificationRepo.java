package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepo extends MongoRepository<Notification, Long> {

    List<Notification> findByUsername(String username);
}
