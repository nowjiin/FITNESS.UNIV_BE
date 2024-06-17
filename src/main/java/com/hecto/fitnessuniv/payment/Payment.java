package com.hecto.fitnessuniv.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resultCd;
    private String errCd;
    private String resultMsg;
    private String mercntId;
    private String ordNo;
    private String authNo;
    private String trPrice;
    private String discntPrice;
    private String payPrice;
    private String trDay;
    private String trTime;
}
