package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.OrderDto;
import com.project.ecommerce.dto.OrderItemDto;
import com.project.ecommerce.entitiy.Order;
import com.project.ecommerce.entitiy.OrderItem;
import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.entitiy.Status;
import com.project.ecommerce.exceprion.ProductException;
import com.project.ecommerce.repo.OrderItemRepository;
import com.project.ecommerce.repo.OrderRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.repo.UserRepository;
import com.project.ecommerce.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> new OrderDto(order))
                .toList();
    }

    @Override
    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(order -> new OrderDto(order));
    }


    @Override
    public OrderDto saveOrder(OrderDto orderDto) {

        // get user from security context holder
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("username: {}", username);

        Order order = modelMapper.map(orderDto, Order.class);

        // set user to order
        userRepository.findOneByUsername(username)
                .ifPresent(order::setUser);

        // check if product exists in order
        for (OrderItemDto o : orderDto.getOrderItems()) {
            Optional<ProductVariant> p = productVariantRepository.findById(o.getProduct_id());
            if (p.isEmpty()) {
                throw new ProductException("Product not found");
            }
        }

        // check if order items is empty
        if(orderDto.getOrderItems().isEmpty()) {
            throw new ProductException("Order items cannot be empty");
        }

        // set status if order is new
        if (orderDto.getStatus() == null) {
            order.setStatus(Status.PENDING);
        }

        // save order
        orderRepository.save(order);
        saveOrderItems(order, orderDto.getOrderItems());

        return new OrderDto(order);
    }

    private void saveOrderItems(Order order, List<OrderItemDto> orderItems) {
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItems.forEach(orderItemDto -> {
            ProductVariant productVariant = productVariantRepository.findById(orderItemDto.getProduct_id())
                    .orElseThrow(() -> new ProductException("Product not found"));
            OrderItem orderItem = new OrderItem(productVariant, orderItemDto.getQuantity(), order);
            orderItemList.add(orderItem);
            orderItemRepository.save(orderItem);
        });

        // set order items for response
        order.setOrderItems(orderItemList);
    }
}
