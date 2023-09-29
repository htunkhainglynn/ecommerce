package com.project.ecommerce.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.entitiy.Notification;
import com.project.ecommerce.entitiy.Status;
import com.project.ecommerce.service.NotificationService;
import com.project.ecommerce.service.QueueInfoService;
import com.project.ecommerce.vo.OrderDetailVo;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class NotificationAspect {

    private final NotificationService notificationService;

    private final QueueInfoService queueInfoService;

    private final RabbitTemplate rabbitTemplate;

    private final DirectExchange directExchange;

    @Autowired
    public NotificationAspect(NotificationService notificationService,
                              QueueInfoService queueInfoService,
                              RabbitTemplate rabbitTemplate,
                              DirectExchange directExchange) {
        this.notificationService = notificationService;
        this.queueInfoService = queueInfoService;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }

    @AfterReturning(
            pointcut = "execution(* com.project.ecommerce.controller.OrderController.addOrder(..)) && args(orderDto)",
            argNames = "result, orderDto",
            returning = "result"
    )
    public void sendNotificationAfterOrderCreation(Object result, OrderDetailDto orderDto) throws JsonProcessingException {
        if (result instanceof ResponseEntity) {
            ResponseEntity<OrderDetailVo> responseEntity = (ResponseEntity<OrderDetailVo>) result;

            // Check if the status code indicates success (e.g., 2xx range)
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                OrderDetailVo orderVo = responseEntity.getBody();
                // Send a notification using the notificationService
                assert orderVo != null;
                sendNotification(orderVo.getOrderId(), "New Order Arrived!", getAdminRoutingKey());
            }
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.project.ecommerce.controller.OrderController.updateOrderStatus(..))",
            returning = "updatedResult"
    )
    public void sendNotificationAfterOrderStatusUpdate(Object updatedResult) throws JsonProcessingException {
        if (updatedResult instanceof ResponseEntity) {
            ResponseEntity<OrderDetailVo> responseEntity = (ResponseEntity<OrderDetailVo>) updatedResult;

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                OrderDetailVo orderDetailVo = responseEntity.getBody();
                assert orderDetailVo != null;

                if (orderDetailVo.getStatus().equals(Status.SHIPPED)) {
                    // get user routing key
                    String userRoutingKey = queueInfoService.getRoutingKeyByUsername(
                                                                orderDetailVo.getUser().getUsername());
                    // Send a notification using the notificationService
                    sendNotification(orderDetailVo.getOrderId(), "Order shipped", userRoutingKey);
                } else if (orderDetailVo.getStatus().equals(Status.RECEIVED)) {
                    // Send a notification using the notificationService
                    sendNotification(orderDetailVo.getOrderId(), "Order received", getAdminRoutingKey());
                }
            }
        }
    }

    private String getAdminRoutingKey() {
        return queueInfoService.getRoutingKeyByUsername("admin");
    }

    private void sendNotification(long orderId, String message, String routingKey) throws JsonProcessingException {
        // send notification to admin
        Map<String, Object> notification = new HashMap<>();
        notification.put("message", message);
        notification.put("orderId", orderId);

        // change map to json
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonNotification = objectMapper.writeValueAsString(notification);

        // save notification to database
        notificationService.saveNotification(Notification.builder().orderId(orderId).message(message).date(LocalDate.now()).build());

        // send notification to admins using topic exchange
        rabbitTemplate.convertAndSend(directExchange.getName(), routingKey, jsonNotification);
    }
}
