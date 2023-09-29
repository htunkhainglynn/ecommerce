package com.project.ecommerce.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.service.ProductVariantService;
import com.project.ecommerce.vo.OrderDetailVo;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class ProductQuantityUpdateAspect {

    private final ProductService productService;

    private final ProductVariantService productVariantService;

    private final NotificationAspect notificationAspect;

    @Autowired
    public ProductQuantityUpdateAspect(ProductService productService,
                                       ProductVariantService productVariantService,
                                       NotificationAspect notificationAspect) {
        this.productService = productService;
        this.notificationAspect = notificationAspect;
        this.productVariantService = productVariantService;
    }

    @AfterReturning(
            pointcut = "execution(* com.project.ecommerce.api.OrderApi.addOrder(..)) && args(orderDto)",
            returning = "result",
            argNames = "result,orderDto")
    public void updateProductVariantQuantity(Object result, OrderDetailDto orderDto) throws JsonProcessingException {
        if (result instanceof ResponseEntity) {
            ResponseEntity<OrderDetailVo> responseEntity = (ResponseEntity<OrderDetailVo>) result;

            // Check if the status code indicates success (e.g., 2xx range)
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                // update product quantity in database
                Map<Integer, Integer> productQuantityMap = new HashMap<>();

                orderDto.getOrderItems().forEach(orderItemDto -> {
                    Integer productId = orderItemDto.getProductVariantId();
                    Integer quantity = orderItemDto.getQuantity();
                    productQuantityMap.put(productId, quantity);
                });

                productService.updateProductVariantQuantity(productQuantityMap);
            }
        }
    }
}

