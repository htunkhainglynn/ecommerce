package com.project.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.vo.OrderDetailVo;
import com.project.ecommerce.vo.OrderVo;
import com.project.ecommerce.entitiy.Notification;
import com.project.ecommerce.service.NotificationService;
import com.project.ecommerce.service.OrderService;
import com.project.ecommerce.service.QueueInfoService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    private final NotificationService notificationService;

    private final QueueInfoService queueInfoService;

    private final RabbitTemplate rabbitTemplate;

    private final DirectExchange directExchange;


    @Autowired
    public OrderController(OrderService orderService,
                           NotificationService notificationService,
                           QueueInfoService queueInfoService,
                           RabbitTemplate rabbitTemplate,
                           DirectExchange directExchange) {
        this.orderService = orderService;
        this.notificationService = notificationService;
        this.queueInfoService = queueInfoService;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }

    @GetMapping
    public Page<OrderVo> getAllOrders(@RequestParam(required = false) String keyword,
                                      @RequestParam Optional<Integer> page,
                                      @RequestParam Optional<Integer> size) {
        return orderService.getAllOrders(keyword, page, size);
    }

    @PostMapping
    public ResponseEntity<OrderDetailVo> addOrder(@RequestBody OrderDetailDto orderDto) throws JsonProcessingException {
        OrderDetailVo result = orderService.saveOrder(orderDto);
        String routingKey = getAdminRoutingKey();
        sendNotification(result, "New order arrived!", routingKey);
        return ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailVo> getOrderById(@PathVariable Long id) {
        Optional<OrderDetailVo> result = orderService.getOrderById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private String getAdminRoutingKey() {
        return queueInfoService.getRoutingKeyByUsername("admin");
    }

    private void sendNotification(OrderDetailVo order, String message, String routingKey) throws JsonProcessingException {
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
        rabbitTemplate.convertAndSend(directExchange.getName(), routingKey, jsonNotification);
    }
}
