package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.QueueInfo;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QueueInfoRepository extends JpaRepository<QueueInfo, Long> {

    @Query("SELECT q.routingKey FROM QueueInfo q WHERE q.user.username = :username")
    Optional<String> findRoutingKeyByUsername(String username);

    @Query("SELECT q.queueName FROM QueueInfo q WHERE q.user.username = :username")
    String findQueueNameByUsername(String username);
}
