package com.zarubovandlevchenko.lb1.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

    @Column(name = "expired_at", nullable = false)
    private LocalDate expiredAt;

    @Column(name = "cvv", nullable = false, length = 3)
    private String cvv;

    @Column(name = "notify", nullable = false)
    private Boolean notify = false;

    @Column(name = "limit")
    private BigDecimal limit;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "is_freeze", nullable = false)
    private Boolean isFreeze = false;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = false;

    @Column(name = "pin", nullable = false, length = 4)
    private String pin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModal user;

}