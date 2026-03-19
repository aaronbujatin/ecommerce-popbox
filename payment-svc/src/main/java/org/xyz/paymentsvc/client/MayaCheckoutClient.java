package org.xyz.paymentsvc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.xyz.paymentsvc.dto.MayaCheckoutReqPayload;
import org.xyz.paymentsvc.dto.MayaCheckoutRespPayload;

@FeignClient(name = "maya-checkout-service", url = "https://pg-sandbox.paymaya.com")
public interface MayaCheckoutClient {


    @PostMapping("/checkout/v1/checkouts")
    MayaCheckoutRespPayload checkoutPayment(
            @RequestHeader(name = "Authorization") String auth,
            @RequestBody MayaCheckoutReqPayload payload
    );
}
