package com.zarubovandlevchenko.lb1.repository;

import com.zarubovandlevchenko.lb1.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByCardId(Long cardId);
    Card findByCardNumber(String cardNumber);
}
