package com.project.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecommerce.dto.OrderDto;
import com.project.ecommerce.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ok(orderService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderDto orderDto) throws JsonProcessingException {
        OrderDto result = orderService.saveOrder(orderDto);
        Map<String, Object> notification = new HashMap<>();
        notification.put("message", "New order arrived!");
        notification.put("orderId", result.getOrderId());

        // change map to json
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonNotification = objectMapper.writeValueAsString(notification);

        rabbitTemplate.convertAndSend("ecommerce-exchange", "customer-admin", jsonNotification);
        return ok(result);
    }
}
