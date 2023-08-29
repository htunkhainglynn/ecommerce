package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.StripeDto;
import com.project.ecommerce.dto.StripeItemDto;
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

    public Session createSession(StripeDto stripeDto) throws StripeException {
        String successURL = baseURL + "/payment/success";

        String failureURL = baseURL + "/payment/failed";

        Stripe.apiKey = stripeSecretKey;

        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();

        stripeDto.getOrderItems().forEach(orderItemDto -> sessionItemList.add(createSessionLineItem(orderItemDto)));

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failureURL)
                .addAllLineItem(sessionItemList)
                .setSuccessUrl(successURL)
                .build();
        return Session.create(params);
    }

    private SessionCreateParams.LineItem createSessionLineItem(StripeItemDto stripeItemDto) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(stripeItemDto))
                .setQuantity(Long.parseLong(String.valueOf(stripeItemDto.getQuantity())))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(StripeItemDto stripeItemDto) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(stripeItemDto.getProductName())
                                .build())
                .setUnitAmount(stripeItemDto.getPrice() * 100L).build();
    }
}
