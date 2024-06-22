package com.ESN_Poliapp.Proiect;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/charge")
    public ResponseEntity<String> chargeCard(@RequestBody ChargeRequest chargeRequest) {
        try {
            String chargeId = paymentService.processPayment(chargeRequest.getToken(), chargeRequest.getAmount(), chargeRequest.getCurrency());
            return ResponseEntity.ok("Payment successful. Charge ID: " + chargeId);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed: " + e.getMessage());
        }
    }
}

