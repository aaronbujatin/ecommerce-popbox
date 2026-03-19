package org.xyz.paymentsvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xyz.paymentsvc.dto.MayaCheckoutReqPayload;
import org.xyz.paymentsvc.dto.MayaCheckoutRespPayload;
import org.xyz.paymentsvc.dto.NotifyCheckoutPayload;
import org.xyz.paymentsvc.service.PaymentService;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

//    @GetMapping("/notify-checkout")
//    public ResponseEntity<String> notifyCheckout(@RequestBody NotifyCheckoutPayload notifyCheckoutPayload) {
//        var body = productService.getProductUnitByProductId(productUnitId);
//        return ResponseEntity.ok(body);
//    }


    @PostMapping("/checkout/maya")
    public ResponseEntity<MayaCheckoutRespPayload> checkoutMayaCard(@RequestBody MayaCheckoutReqPayload mayaCheckoutReqPayload) throws JsonProcessingException {
        return ResponseEntity.ok(paymentService.checkoutPayment(mayaCheckoutReqPayload));
    }

    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess() {
        return ResponseEntity.ok("Payment Success");
    }

    @GetMapping("/failed")
    public ResponseEntity<String> paymentFailed() {
        return ResponseEntity.ok("Payment Failed");
    }
}
