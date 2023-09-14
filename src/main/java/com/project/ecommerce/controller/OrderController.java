package com.project.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.entitiy.Notification;
import com.project.ecommerce.entitiy.Status;
import com.project.ecommerce.service.NotificationService;
import com.project.ecommerce.service.OrderService;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.service.QueueInfoService;
import com.project.ecommerce.vo.OrderDetailVo;
import com.project.ecommerce.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@Api(value = "Order Management")
public class OrderController {

    private final OrderService orderService;

    private final ProductService productService;

    private final NotificationService notificationService;

    private final QueueInfoService queueInfoService;

    private final RabbitTemplate rabbitTemplate;

    private final DirectExchange directExchange;

    @Autowired
    public OrderController(OrderService orderService,
                           ProductService productService,
                           NotificationService notificationService,
                           QueueInfoService queueInfoService,
                           RabbitTemplate rabbitTemplate,
                           DirectExchange directExchange) {
        this.orderService = orderService;
        this.productService = productService;
        this.notificationService = notificationService;
        this.queueInfoService = queueInfoService;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Requires ADMIN authority")
    public Page<OrderVo> getAllOrders(@RequestParam(required = false) String keyword,
                                      @RequestParam Optional<Integer> page,
                                      @RequestParam Optional<Integer> size) {
        return orderService.getAllOrders(keyword, page, size);
    }

    @PostMapping
    @Operation(summary = "Create order", description = "Requires USER authority")
    public ResponseEntity<OrderDetailVo> addOrder(@RequestBody OrderDetailDto orderDto) throws JsonProcessingException {

        // update product quantity in database
        Map<Integer, Integer> productQuantityMap = new HashMap<>();

        orderDto.getOrderItems().forEach(orderItemDto -> {
            Integer productId = orderItemDto.getProduct_id();
            Integer quantity = orderItemDto.getQuantity();
            productQuantityMap.put(productId, quantity);
        });

        productService.updateProductQuantity(productQuantityMap);

        // set order status
        orderDto.setStatus(Status.PENDING);

        OrderDetailVo result = orderService.saveOrder(orderDto);

        String routingKey = getAdminRoutingKey();
        log.info("routingKey: {}", routingKey);
        sendNotification(result, "New order arrived!", routingKey);
        return ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id", description = "Requires ADMIN or USER authority")
    public ResponseEntity<OrderDetailVo> getOrderById(@PathVariable Long id) {
        Optional<OrderDetailVo> result = orderService.getOrderById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order by id", description = "Requires ADMIN or USER authority")
    public ResponseEntity<OrderDetailVo> updateOrderStatus(@PathVariable Long id) throws JsonProcessingException {

        Optional<OrderDetailVo> result = orderService.getOrderById(id);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        OrderDetailVo updatedResult;

        // if order status is pending, change it to delivered
        if(result.get().getStatus().equals(Status.PENDING)) {
            updatedResult = orderService.updateStatue(id, Status.SHIPPED);

            // send notification to customer
            String username = result.get().getUser().getUsername();
            log.info("username: {}", username);
            String routingKey = queueInfoService.getRoutingKeyByUsername(username);
            sendNotification(result.get(), "Order has been shipped!", routingKey);
        } else {
            updatedResult = orderService.updateStatue(id, Status.RECEIVED);

            // send notification to admin
            String routingKey = getAdminRoutingKey();
            sendNotification(result.get(), "Order has been received!", routingKey);
        }

        return ok(updatedResult);
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
        objectMapper.registerModule(new JavaTimeModule());
        String jsonNotification = objectMapper.writeValueAsString(notification);

        // save notification to database
        notificationService.saveNotification(Notification.builder().order(order).message(message).build());

        // send notification to admins using topic exchange
        rabbitTemplate.convertAndSend(directExchange.getName(), routingKey, jsonNotification);
    }
}
