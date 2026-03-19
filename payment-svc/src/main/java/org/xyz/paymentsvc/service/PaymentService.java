package org.xyz.paymentsvc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.xyz.paymentsvc.dto.MayaCheckoutReqPayload;
import org.xyz.paymentsvc.dto.MayaCheckoutRespPayload;
import org.xyz.paymentsvc.dto.NotifyCheckoutPayload;

public interface PaymentService {

    void notifyPayment(NotifyCheckoutPayload notifyCheckoutPayload) throws JsonProcessingException;
    MayaCheckoutRespPayload checkoutPayment(MayaCheckoutReqPayload mayaCheckoutReqPayload) throws JsonProcessingException;




}
