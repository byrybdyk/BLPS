package com.zarubovandlevchenko.lb1.repository.dbcard;

import com.zarubovandlevchenko.lb1.model.dbcard.Card;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByCardId(Long cardId);
    Card findByCardNumber(String cardNumber);
    //TODO переделать запрос
    List<Card> findAllByUser(UserModal user);
}
