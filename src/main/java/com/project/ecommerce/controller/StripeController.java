package com.project.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.ecommerce.dto.StripeDto;
import com.project.ecommerce.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/stripe")
public class StripeController {

    private final StripeService stripeService;
    @Autowired
    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<String> createCheckoutSession(@RequestBody StripeDto stripeDto) throws JsonProcessingException, StripeException {
        Session session = stripeService.createSession(stripeDto);
        return ok(session.getUrl());
    }
}