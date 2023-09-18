package com.project.ecommerce.aspects;

import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.vo.OrderDetailVo;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ProductQuantityUpdateAspect {

    private final ProductService productService;

    @Autowired
    public ProductQuantityUpdateAspect(ProductService productService) {
        this.productService = productService;
    }

    @AfterReturning(
            pointcut = "execution(* com.project.ecommerce.controller.OrderController.addOrder(..)) && args(orderDto)",
            returning = "result",
            argNames = "result,orderDto")
    public void updateProductVariantQuantity(Object result, OrderDetailDto orderDto) {
        // Check if the result is an instance of OrderDetailVo (or your actual return type)
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

