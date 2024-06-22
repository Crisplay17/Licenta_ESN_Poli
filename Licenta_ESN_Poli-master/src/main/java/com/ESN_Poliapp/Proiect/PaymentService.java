package com.ESN_Poliapp.Proiect;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public String processPayment(String token, double amount, String currency) throws StripeException {
        try {
            Stripe.apiKey = stripeApiKey;

            Map<String, Object> params = new HashMap<>();
            params.put("amount", (int) (amount * 100)); // Amount in cents
            params.put("currency", currency);
            params.put("source", token); // obtained with Stripe.js
            params.put("description", "Payment for ESNcard");

            Charge charge = Charge.create(params);
            return charge.getId();
        } catch (StripeException e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // Throw the exception to be handled by the controller
            throw e;
        }
    }
}



