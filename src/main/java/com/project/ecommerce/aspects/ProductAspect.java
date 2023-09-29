package com.project.ecommerce.aspects;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.exception.ProductException;
import com.project.ecommerce.service.CloudinaryService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ProductAspect {

    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductAspect(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @Before("execution(* com.project.ecommerce.api.ProductApi.createProduct(..))")
    public void uploadProductImage(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        ProductDto product = (ProductDto) args[0];
        HttpServletRequest request = (HttpServletRequest) args[1];

        product.getProductVariants().forEach(
                productVariant -> {
                    try {
                        cloudinaryService.uploadAndSetUrl(product.getSku(), productVariant, request);
                    } catch (Exception e) {
                        throw new ProductException("Error uploading image");
                    }
                }
        );
    }
}
