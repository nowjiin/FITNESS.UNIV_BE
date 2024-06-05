package com.hecto.fitnessuniv.payment;

import java.util.Map;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*") // 모든 도메인 허용
public class PaymentCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackController.class);

    @Autowired private PaymentRepository paymentRepository;

    @Transactional
    @PostMapping("/paybutton")
    public ResponseEntity<String> handlePaymentCallback(@RequestParam Map<String, String> params) {
        logger.info("Received payment callback with params: {}", params);

        String resultCd = params.get("resultCd");
        String errCd = params.get("errCd");
        String resultMsg = params.get("resultMsg");
        String mercntId = params.get("mercntId");
        String ordNo = params.get("ordNo");
        String authNo = params.get("authNo");
        String trPrice = params.get("trPrice");
        String discntPrice = params.get("discntPrice");
        String payPrice = params.get("payPrice");
        String trDay = params.get("trDay");
        String trTime = params.get("trTime");

        Payment payment = new Payment();
        payment.setResultCd(resultCd);
        payment.setErrCd(errCd);
        payment.setResultMsg(resultMsg);
        payment.setMercntId(mercntId);
        payment.setOrdNo(ordNo);
        payment.setAuthNo(authNo);
        payment.setTrPrice(trPrice);
        payment.setDiscntPrice(discntPrice);
        payment.setPayPrice(payPrice);
        payment.setTrDay(trDay);
        payment.setTrTime(trTime);

        paymentRepository.save(payment);

        return ResponseEntity.ok("OK");
    }
}
