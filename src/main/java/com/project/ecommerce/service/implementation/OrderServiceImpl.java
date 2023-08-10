package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.OrderDto;
import com.project.ecommerce.dto.OrderItemDto;
import com.project.ecommerce.entitiy.Order;
import com.project.ecommerce.entitiy.OrderItem;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.exceprion.ProductException;
import com.project.ecommerce.repo.OrderItemRepository;
import com.project.ecommerce.repo.OrderRepository;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .toList();
    }


    @Override
    public OrderDto saveOrder(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        order = orderRepository.save(order);
        saveOrderItems(order, orderDto.getOrderItems());
        return modelMapper.map(order, OrderDto.class);
    }

    private void saveOrderItems(Order order, List<OrderItemDto> orderItems) {
        orderItems.forEach(orderItemDto -> {
            log.info("Saving order item with product id: {}", orderItemDto.getProduct_id());
            ProductVariant productVariant = productVariantRepository.findById(orderItemDto.getProduct_id())
                    .orElseThrow(() -> new ProductException("Product not found"));
            OrderItem orderItem = new OrderItem(productVariant, orderItemDto.getQuantity(), order);
            orderItemRepository.save(orderItem);
        });
    }
}
