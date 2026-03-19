package org.xyz.paymentsvc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xyz.paymentsvc.client.MayaCheckoutClient;
import org.xyz.paymentsvc.client.OrderClient;
import org.xyz.paymentsvc.dto.MayaCheckoutReqPayload;
import org.xyz.paymentsvc.dto.MayaCheckoutRespPayload;
import org.xyz.paymentsvc.dto.NotifyCheckoutPayload;
import org.xyz.paymentsvc.entity.Payment;
import org.xyz.paymentsvc.enums.PaymentStatus;
import org.xyz.paymentsvc.repository.PaymentRepository;
import org.xyz.paymentsvc.service.PaymentService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final OrderClient orderClient;
    private final MayaCheckoutClient mayaCheckoutClient;

    @Override
    public void notifyPayment(NotifyCheckoutPayload notifyCheckoutPayload) throws JsonProcessingException {
        var payment = Payment.builder()
                .amount(notifyCheckoutPayload.amount())
                .status(PaymentStatus.valueOf(notifyCheckoutPayload.status()))
                .transactionId(Long.valueOf(notifyCheckoutPayload.id()))
                .orderId(Long.valueOf(notifyCheckoutPayload.requestReferenceNumber()))
                .stringifyNotifPayload(objectMapper.writeValueAsString(notifyCheckoutPayload))
                .build();

        paymentRepository.save(payment);
    }

    @Override
    public MayaCheckoutRespPayload checkoutPayment(MayaCheckoutReqPayload mayaCheckoutReqPayload) {
        var username = "pk-Z0OSzLvIcOI2UIvDhdTGVVfRSSeiGStnceqwUE7n0Ah:";
        // Convert the original string to bytes
        byte[] sourceBytes = username.getBytes(StandardCharsets.UTF_8);

        // Get the Base64 encoder
        Base64.Encoder encoder = Base64.getEncoder();

        // Encode the bytes to a Base64 string
        String basicAuth = "Basic " + encoder.encodeToString(sourceBytes);

        System.out.println("test123: " + basicAuth + "payload: " + mayaCheckoutReqPayload);
        return  mayaCheckoutClient.checkoutPayment(basicAuth, mayaCheckoutReqPayload);
    }


}
