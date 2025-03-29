package com.zarubovandlevchenko.lb1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "user_card")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "card_number", nullable = false, length = 20)
    private String cardNumber;

    @Column(name = "expired_at", nullable = false)
    private LocalDate expiredAt;

    @Column(name = "cvv", nullable = false, length = 3)
    private String cvv;

    @Column(name = "notify", nullable = false)
    private Boolean notify = false;

    @Column(name = "limitcash")
    private Double limit;

    @Column(name = "balance", nullable = false)
    private Double balance = (double) 0;

    @Column(name = "is_freeze", nullable = false)
    private Boolean isFreeze = false;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = false;

    @Column(name = "pin", nullable = false, length = 4)
    private String pin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModal user;
}