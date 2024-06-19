package com.hecto.fitnessuniv.payment;

import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.hecto.fitnessuniv.paymentapproval.PaymentApproval;
import com.hecto.fitnessuniv.paymentapproval.PaymentApprovalRepository;
import com.hecto.fitnessuniv.provider.JwtProvider;

@RestController
@CrossOrigin(origins = "*") // 모든 도메인 허용
public class PaymentCallbackController {

    @Value("${front-url}")
    private String frontUrl;

    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackController.class);

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private PaymentApprovalRepository paymentApprovalRepository;
    @Autowired private RestTemplate restTemplate;
    @Autowired private JwtProvider jwtProvider;

    // 결제 콜백을 처리하는 메서드
    @Transactional
    @PostMapping("/paybutton")
    public ResponseEntity<Void> handlePaymentCallback(@RequestParam Map<String, String> params) {
        logger.info("Received payment callback with params: {}", params);

        // 콜백 파라미터들을 가져옴
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

        // Payment 객체를 생성하고 파라미터 값을 설정
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

        // Payment 객체를 데이터베이스에 저장
        paymentRepository.save(payment);

        // 리다이렉트 URL에 필요한 파라미터 추가
        String redirectUrl =
                String.format(
                        frontUrl + "/payment/success?mercntId=%s&authNo=%s&reqDay=%s&reqTime=%s",
                        mercntId,
                        authNo,
                        trDay,
                        trTime);

        // 리다이렉트 응답을 반환
        return ResponseEntity.status(302).header("Location", redirectUrl).build();
    }

    // 결제 승인 결과를 처리하는 메서드
    @Transactional
    @PostMapping("/payment/approval")
    public ResponseEntity<String> handlePaymentApproval(@RequestBody Map<String, String> params) {
        logger.info("Received payment approval with params: {}", params);

        // 승인 파라미터들을 가져옴
        String resultCd = params.get("resultCd");
        String errCd = params.get("errCd");
        String resultMsg = params.get("resultMsg");
        String mercntId = params.get("mercntId");
        String ordNo = params.get("ordNo");
        String trNo = params.get("trNo");
        String trPrice = params.get("trPrice");
        String discntPrice = params.get("discntPrice");
        String payPrice = params.get("payPrice");
        String criPrice = params.get("criPrice");
        String criTaxVatPrice = params.get("criTaxVatPrice");
        String criDutyFreePrice = params.get("criDutyFreePrice");
        String trDay = params.get("trDay");
        String trTime = params.get("trTime");
        String userId = params.get("userId");
        String mentorUserName = params.get("mentorUserName");

        // PaymentApproval 객체를 생성하고 승인 파라미터 값을 설정
        PaymentApproval paymentApproval = new PaymentApproval();
        paymentApproval.setResultCd(resultCd);
        paymentApproval.setErrCd(errCd);
        paymentApproval.setResultMsg(resultMsg);
        paymentApproval.setMercntId(mercntId);
        paymentApproval.setOrdNo(ordNo);
        paymentApproval.setTrNo(trNo);
        paymentApproval.setTrPrice(trPrice);
        paymentApproval.setDiscntPrice(discntPrice);
        paymentApproval.setPayPrice(payPrice);
        paymentApproval.setCriPrice(criPrice);
        paymentApproval.setCriTaxVatPrice(criTaxVatPrice);
        paymentApproval.setCriDutyFreePrice(criDutyFreePrice);
        paymentApproval.setTrDay(trDay);
        paymentApproval.setTrTime(trTime);
        paymentApproval.setUserId(userId); // userId 값을 설정
        paymentApproval.setMentorUserName(mentorUserName);

        // PaymentApproval 객체를 새로운 데이터베이스에 저장
        paymentApprovalRepository.save(paymentApproval);

        return ResponseEntity.ok("Payment approval received and saved");
    }

    // Settlebank API를 대신 호출하는 프록시 메서드
    @PostMapping("/proxy/settlebank")
    public ResponseEntity<String> proxySettlebank(@RequestBody Map<String, String> params) {
        logger.info("Proxying request to Settlebank with params: {}", params);

        String url = "https://tbezauthapi.settlebank.co.kr/v3/APIPayApprov.do";
        ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);
        return response;
    }

    // userId로 PaymentApproval을 조회하는 메서드
    @GetMapping("/payment/approval")
    public ResponseEntity<List<PaymentApproval>> getPaymentApprovalsByUserId(
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        if (!jwtProvider.validate(jwtToken)) {
            return ResponseEntity.status(401).build();
        }
        String userId = jwtProvider.getUserIdFromToken(jwtToken);
        logger.info("Fetching payment approvals for userId: {}", userId);
        List<PaymentApproval> paymentApprovals = paymentApprovalRepository.findByUserId(userId);
        return ResponseEntity.ok(paymentApprovals);
    }
}
