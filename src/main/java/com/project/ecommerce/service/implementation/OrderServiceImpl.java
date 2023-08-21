package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.OrderDetailDto;
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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
    public List<OrderDto> getAllOrders(
                            String keyword,
                            Optional<Integer> page,
                            Optional<Integer> size) {
        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                size.orElse(10));

        Specification<Order> specification = (root, query, cb) -> cb.conjunction();

        Predicate<String> isDouble = s -> {
            try {
                Double.parseDouble(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        };

        if (keyword != null && !isDouble.test(keyword)) {
            specification = specification.and((root, query, cb) ->
                    cb.or(
                            cb.like(root.get("user").get("name"), "%" + keyword + "%"),
                            cb.like(root.get("status"), "%" + keyword + "%")
                    ));
        }

        if (StringUtils.hasLength(keyword) && isDouble.test(keyword)) {
            specification = specification.and((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("totalPrice"), Double.parseDouble(keyword))
            );
        }

        return orderRepository.findAll(specification, pageRequest)
                .map(OrderDto::new)
                .toList();
    }

    @Override
    public Optional<OrderDetailDto> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(OrderDetailDto::new);
    }

    @Transactional
    @Override
    public OrderDetailDto saveOrder(OrderDetailDto orderDetailDto) {

        // get user from security context holder
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("username: {}", username);

        Order order = modelMapper.map(orderDetailDto, Order.class);

        // set user to order
        userRepository.findOneByUsername(username)
                .ifPresent(order::setUser);

        // check if product exists in order
        for (OrderItemDto o : orderDetailDto.getOrderItems()) {
            Optional<ProductVariant> p = productVariantRepository.findById(o.getProduct_id());
            if (p.isEmpty()) {
                throw new ProductException("Product not found");
            }
        }

        // check if order items is empty
        if(orderDetailDto.getOrderItems().isEmpty()) {
            throw new ProductException("Order items cannot be empty");
        }

        // set status if order is new
        if (orderDetailDto.getStatus() == null) {
            order.setStatus(Status.PENDING);
        }

        // save order
        orderRepository.save(order);
        saveOrderItems(order, orderDetailDto.getOrderItems());

        return new OrderDetailDto(order);
    }

    @Transactional
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
