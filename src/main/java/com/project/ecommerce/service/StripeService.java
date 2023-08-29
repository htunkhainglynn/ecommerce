package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderDetailDto;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface StripeService {
    public Session createSession(OrderDetailDto orderDto) throws StripeException;
}
