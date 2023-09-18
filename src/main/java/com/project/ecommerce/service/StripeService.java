package com.project.ecommerce.service;

import com.project.ecommerce.dto.StripeDto;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

public interface StripeService {
    Session createSession(StripeDto stripeDto) throws StripeException;
}
