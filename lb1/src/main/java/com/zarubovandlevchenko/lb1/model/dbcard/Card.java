package com.zarubovandlevchenko.lb1.model.dbcard;

import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_card")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cards_seq")
    @SequenceGenerator(name = "cards_seq", sequenceName = "cards_id_seq", allocationSize = 1)
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

    @Column(name = "user_id", nullable = false)
    private Long user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}