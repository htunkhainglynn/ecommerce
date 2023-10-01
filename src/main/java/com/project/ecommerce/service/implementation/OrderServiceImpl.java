package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.dto.OrderItemDto;
import com.project.ecommerce.entitiy.*;
import com.project.ecommerce.exception.ProductException;
import com.project.ecommerce.repo.*;
import com.project.ecommerce.service.AddressService;
import com.project.ecommerce.service.OrderService;
import com.project.ecommerce.vo.OrderDetailVo;
import com.project.ecommerce.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductVariantRepository productVariantRepository;

    private final UserRepository userRepository;

    private final OrderItemRepository orderItemRepository;

    private final AddressRepo addressRepo;

    private final AddressService addressService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductVariantRepository productVariantRepository,
                            UserRepository userRepository,
                            OrderItemRepository orderItemRepository,
                            AddressRepo addressRepo,
                            AddressService addressService) {
        this.orderRepository = orderRepository;
        this.productVariantRepository = productVariantRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.addressRepo = addressRepo;
        this.addressService = addressService;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public Page<OrderVo> getAllOrders(
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
                .map(OrderVo::new);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('USER')")
    @Transactional(readOnly = true)
    @Override
    public Optional<OrderDetailVo> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(OrderDetailVo::new);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('USER')")
    @Override
    public OrderDetailVo updateStatus(Long id, Status status) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new ProductException("Order not found");
        }

        order.get().setStatus(status);
        orderRepository.save(order.get());
        return new OrderDetailVo(order.get());
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Override
    public Optional<OrderDetailVo> getOrderByIdWithUsername(Long id, String username) {
        return orderRepository.findByIdAndUserUsername(id, username)
                .map(OrderDetailVo::new);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN') or hasAnyAuthority('USER')")
    @Override
    public Optional<List<OrderItemDto>> getOrderItemsByOrderId(Long id) {
        List<OrderItemDto> orderItemDto = orderRepository
                                                .getOrderItemsByOrderId(id)
                                                .stream()
                                                .map(OrderItemDto::new)
                                                .toList();
        return Optional.of(orderItemDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN') or hasAnyAuthority('USER')")
    @Override
    public Page<OrderVo> getAllOrdersByUsername(String username, Optional<Integer> page, Optional<Integer> size) {
        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                size.orElse(10));

        return orderRepository.findAllByUserUsernameLike(username, pageRequest).map(OrderVo::new);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Override
    public OrderDetailVo saveOrder(OrderDetailDto orderDetailDto) {

        // get user from security context holder
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Address address;

        // get address by id
        if (orderDetailDto.getAddressId() != null) {
            log.info("address id: {}", orderDetailDto.getAddressId());
            address = addressRepo.findById(orderDetailDto.getAddressId())
                    .orElseThrow(() -> new ProductException("Address not found"));
        } else {
            log.info("address: {}", orderDetailDto.getAddress());
            address = addressService.saveAddressByUsername(orderDetailDto.getAddress(), username);
        }

        Order order = new Order(orderDetailDto);

        // set address to order
        order.setAddress(address);

        // set order status to pending
        order.setStatus(Status.PENDING);

        // set order date
        order.setOrderDate(LocalDate.now());

        // set user to order
        userRepository.getReferenceByUsername(username)
                .ifPresent(order::setUser);

        // check if product exists in order
        for (OrderItemDto o : orderDetailDto.getOrderItems()) {
            Optional<ProductVariant> p = productVariantRepository.findById(o.getProductVariantId());
            if (p.isEmpty()) {
                throw new ProductException("Product not found");
            }
        }

        // check if order items is empty
        if(orderDetailDto.getOrderItems().isEmpty()) {
            throw new ProductException("Order items cannot be empty");
        }

        // save order
        orderRepository.save(order);
        saveOrderItems(order, orderDetailDto.getOrderItems());

        return new OrderDetailVo(order);
    }

    private void saveOrderItems(Order order, List<OrderItemDto> orderItems) {
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItems.forEach(orderItemDto -> {
            ProductVariant productVariant = productVariantRepository.findById(orderItemDto.getProductVariantId())
                    .orElseThrow(() -> new ProductException("Product not found"));
            OrderItem orderItem = new OrderItem(productVariant, orderItemDto.getQuantity(), order);
            orderItemList.add(orderItem);
            orderItemRepository.save(orderItem);
        });

        // set order items for response
        order.setOrderItems(orderItemList);
    }
}
