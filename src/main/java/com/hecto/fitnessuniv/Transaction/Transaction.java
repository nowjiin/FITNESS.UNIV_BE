package com.hecto.fitnessuniv.Transaction;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction") // 테이블 이름을 지정할 수 있습니다.
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "order_number",
            nullable = false,
            length = 50) // 컬럼 이름, nullable 여부, 길이 등을 지정할 수 있습니다.
    private String ordNo;

    @Column(name = "payment_price", nullable = false)
    private String payPrice;

    @Column(name = "transaction_day", nullable = false)
    private String trDay;

    @Column(name = "transaction_time", nullable = false)
    private String trTime;

    @Column(name = "mentor_name", nullable = false)
    private String mentorName;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
