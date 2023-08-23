package com.project.ecommerce.service;

import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.entitiy.QueueInfo;
import com.project.ecommerce.repo.QueueInfoRepository;
import com.project.ecommerce.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DynamicQueueManager {

    private final RabbitAdmin rabbitAdmin;
    private final QueueInfoRepository queueInfoRepository;
    private final UserRepository userRepository;
    private final DirectExchange directExchange;

    @Autowired
    public DynamicQueueManager(RabbitAdmin rabbitAdmin,
                               QueueInfoRepository queueInfoRepository,
                               UserRepository userRepository,
                               DirectExchange directExchange) {
        this.rabbitAdmin = rabbitAdmin;
        this.queueInfoRepository = queueInfoRepository;
        this.userRepository = userRepository;
        this.directExchange = directExchange;
    }

    public void createQueueForUser(String username) {

        // create queue name
        String queueName = "ecommerce" + '.' + username + '.' + "queue";

        // create binding key
        String routingKey = "ecommerce" + '.' + username + '.' + "key";

        Queue queue = new Queue(queueName, true, false, false);
        rabbitAdmin.declareQueue(queue);

        // find customer
        User user = userRepository.getReferenceByUsername(username).orElseThrow(() -> new RuntimeException("User not found."));

        // save the queue info
        queueInfoRepository.save(QueueInfo.builder()
                .queueName(queueName)
                .routingKey(routingKey)
                .user(user)
                .build());

        // Create binding between queue and exchange
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(routingKey));
    }
}
