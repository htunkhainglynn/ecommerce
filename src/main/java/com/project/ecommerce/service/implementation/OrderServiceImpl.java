package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.OrderDto;
import com.project.ecommerce.dto.OrderItemDto;
import com.project.ecommerce.entitiy.Order;
import com.project.ecommerce.entitiy.OrderItem;
import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.exceprion.ProductException;
import com.project.ecommerce.repo.OrderItemRepository;
import com.project.ecommerce.repo.OrderRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ModelMapper modelMapper;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> new OrderDto(order))
                .toList();
    }


    @Override
    public OrderDto saveOrder(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        for (OrderItemDto o : orderDto.getOrderItems()) {
            Optional<ProductVariant> p = productVariantRepository.findById(o.getProduct_id());
            if (p.isEmpty()) {
                throw new ProductException("Product not found");
            }
        }

        if(orderDto.getOrderItems().isEmpty()) {
            throw new ProductException("Order items cannot be empty");
        }

        orderRepository.save(order);
        saveOrderItems(order, orderDto.getOrderItems());
        return new OrderDto(order);
    }

    private void saveOrderItems(Order order, List<OrderItemDto> orderItems) {
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItems.forEach(orderItemDto -> {
            log.info("Saving order item with product id: {}", orderItemDto.getProduct_id());
            ProductVariant productVariant = productVariantRepository.findById(orderItemDto.getProduct_id())
                    .orElseThrow(() -> new ProductException("Product not found"));
            OrderItem orderItem = new OrderItem(productVariant, orderItemDto.getQuantity(), order);
            orderItemList.add(orderItem);
            orderItemRepository.save(orderItem);
        });
        order.setOrderItems(orderItemList);
    }
}
