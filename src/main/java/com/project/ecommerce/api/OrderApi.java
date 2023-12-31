package com.project.ecommerce.api;

import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.dto.OrderItemDto;
import com.project.ecommerce.entitiy.Status;
import com.project.ecommerce.service.OrderService;
import com.project.ecommerce.utils.PagerResult;
import com.project.ecommerce.vo.OrderDetailVo;
import com.project.ecommerce.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@Api(value = "Order Management")
public class OrderApi {

    private final OrderService orderService;

    @Autowired
    public OrderApi(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Requires ADMIN authority")
    public ResponseEntity<PagerResult<OrderVo>> getAllOrders(@RequestParam(required = false) String keyword,
                                      @RequestParam Optional<Integer> page,
                                      @RequestParam Optional<Integer> size) {
        Page<OrderVo> pageResult =  orderService.getAllOrders(keyword, page, size);
        PagerResult<OrderVo> pagerResult = PagerResult.of(pageResult);
        return ok(pagerResult);
    }

    @PostMapping
    @Operation(summary = "Create order", description = "Requires USER authority")
    public ResponseEntity<OrderDetailVo> addOrder(@RequestBody OrderDetailDto orderDto) {
        return ok(orderService.saveOrder(orderDto));
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Get all orders by username", description = "Requires ADMIN or USER authority")
    public ResponseEntity<PagerResult<OrderVo>> getAllOrdersByUsername(@PathVariable String username,
                                                                       @RequestParam Optional<Integer> page,
                                                                       @RequestParam Optional<Integer> size) {
        Page<OrderVo> pageResult =  orderService.getAllOrdersByUsername(username, page, size);
        PagerResult<OrderVo> pagerResult = PagerResult.of(pageResult);
        return ok(pagerResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id", description = "Requires ADMIN or USER authority")
    public ResponseEntity<OrderDetailVo> getOrderById(@PathVariable Long id) {
        Optional<OrderDetailVo> result = orderService.getOrderById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/order-items/{id}")
    @Operation(summary = "Get order items by order id", description = "Requires ADMIN or USER authority")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable Long id) {
        Optional<List<OrderItemDto>> result = orderService.getOrderItemsByOrderId(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order by id", description = "Requires ADMIN or USER authority")
    public ResponseEntity<OrderDetailVo> updateOrderStatus(@PathVariable Long id) {

        Optional<OrderDetailVo> result = orderService.getOrderById(id);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        OrderDetailVo updatedResult;

        // if order status is pending, change it to delivered
        if(result.get().getStatus().equals(Status.PENDING)) {
            updatedResult = orderService.updateStatus(id, Status.SHIPPED);
        } else {
            updatedResult = orderService.updateStatus(id, Status.RECEIVED);
        }

        return ok(updatedResult);
    }
}
