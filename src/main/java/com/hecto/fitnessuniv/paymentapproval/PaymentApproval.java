package com.hecto.fitnessuniv.paymentapproval;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PaymentApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String resultCd;
    private String errCd;
    private String resultMsg;
    private String mercntId;
    private String ordNo;
    private String trNo;
    private String trPrice;
    private String discntPrice;
    private String payPrice;
    private String criPrice;
    private String criTaxVatPrice;
    private String criDutyFreePrice;
    private String trDay;
    private String trTime;
    private String userId;
    private String mentorUserName;
    private String mentorId;
    private String menteeUserName;
}
