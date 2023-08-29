package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.dto.OrderItemDto;
import com.project.ecommerce.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${base.url}")
    private String baseURL;

    public Session createSession(OrderDetailDto orderDetailDto) throws StripeException {
        String successURL = baseURL + "/payment/success";

        String failureURL = baseURL + "/payment/failed";

        Stripe.apiKey = stripeSecretKey;

        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();

        orderDetailDto.getOrderItems().forEach(orderItemDto -> {
            sessionItemList.add(createSessionLineItem(orderItemDto));
        });

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failureURL)
                .addAllLineItem(sessionItemList)
                .setSuccessUrl(successURL)
                .build();
        return Session.create(params);
    }

    private SessionCreateParams.LineItem createSessionLineItem(OrderItemDto orderItemDto) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(orderItemDto))
                .setQuantity(Long.parseLong(String.valueOf(orderItemDto.getQuantity())))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItemDto orderItemDto) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(orderItemDto.getProductName())
                                .build())
                .setUnitAmount((long)(orderItemDto.getPrice() * 100L)).build();
    }
}
