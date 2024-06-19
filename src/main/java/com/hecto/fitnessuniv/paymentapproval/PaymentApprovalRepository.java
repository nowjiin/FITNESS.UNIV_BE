package com.hecto.fitnessuniv.paymentapproval;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentApprovalRepository extends JpaRepository<PaymentApproval, Long> {
    List<PaymentApproval> findByUserId(String userId);
}
