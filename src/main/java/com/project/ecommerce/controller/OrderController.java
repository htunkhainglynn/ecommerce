package com.project.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.dto.OrderDto;
import com.project.ecommerce.entitiy.Notification;
import com.project.ecommerce.entitiy.Status;
import com.project.ecommerce.service.NotificationService;
import com.project.ecommerce.service.OrderService;
import com.project.ecommerce.service.QueueInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    private final NotificationService notificationService;

    private final QueueInfoService queueInfoService;

    private final RabbitTemplate rabbitTemplate;

    private final DirectExchange directExchange;

    private final TopicExchange topicExchange;

    @Autowired
    public OrderController(OrderService orderService, NotificationService notificationService, QueueInfoService queueInfoService, RabbitTemplate rabbitTemplate, DirectExchange directExchange, TopicExchange topicExchange) {
        this.orderService = orderService;
        this.notificationService = notificationService;
        this.queueInfoService = queueInfoService;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.topicExchange = topicExchange;
    }

    @GetMapping
    public List<OrderDto> getAllOrders(@RequestParam(required = false) String keyword,
                                       @RequestParam Optional<Integer> page,
                                       @RequestParam Optional<Integer> size) {
        return orderService.getAllOrders(keyword, page, size);
    }

    @PostMapping
    public ResponseEntity<OrderDetailDto> addOrder(@RequestBody OrderDetailDto orderDto) throws JsonProcessingException {
        OrderDetailDto result = orderService.saveOrder(orderDto);
        String routingKey = getAdminRoutingKey();
        sendNotification(result, "New order arrived!", routingKey);
        return ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDto> getOrderById(@PathVariable Long id) {
        Optional<OrderDetailDto> result = orderService.getOrderById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailDto> updateOrderStatus(@PathVariable Long id) throws JsonProcessingException {
        Optional<OrderDetailDto> result = orderService.getOrderById(id);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // if order status is pending, change it to delivered
        if(result.get().getStatus().equals(Status.PENDING)) {
            result.get().setStatus(Status.SHIPPED);

            // send notification to customer
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            String routingKey = queueInfoService.getRoutingKeyByUsername(username);
            sendNotification(result.get(), "Order has been shipped!", routingKey);

        } else {
            result.get().setStatus(Status.RECEIVED);

            // send notification to admin
            String routingKey = getAdminRoutingKey();
            sendNotification(result.get(), "Order has been received!", routingKey);
        }

        return ok(orderService.saveOrder(result.get()));
    }

    private String getAdminRoutingKey() {
        return queueInfoService.getRoutingKeyByUsername("admin");
    }

    private void sendNotification(OrderDetailDto order, String message, String routingKey) throws JsonProcessingException {
        // send notification to admin
        Map<String, Object> notification = new HashMap<>();
        notification.put("message", message);
        notification.put("order", order);

        // change map to json
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonNotification = objectMapper.writeValueAsString(notification);

        // save notification to database
        notificationService.saveNotification(Notification.builder().order(order).message(message).build());

        // send notification to admins using topic exchange
        rabbitTemplate.convertAndSend(topicExchange.getName(), routingKey, jsonNotification);
    }
}
