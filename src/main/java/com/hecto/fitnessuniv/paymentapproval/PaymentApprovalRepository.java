package com.hecto.fitnessuniv.paymentapproval;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentApprovalRepository extends JpaRepository<PaymentApproval, Long> {
    Optional<PaymentApproval> findByOrdNo(String ordNo);
}
