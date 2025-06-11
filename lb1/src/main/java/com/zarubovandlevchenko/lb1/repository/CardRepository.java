package com.zarubovandlevchenko.lb1.repository;

import com.zarubovandlevchenko.lb1.model.dbcard.Card;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByCardId(Long cardId);
    Card findByCardNumber(String cardNumber);
    //TODO переделать запрос
    List<Card> findAllByUser(Long id);
    @Query("SELECT count(c) from Card c WHERE c.createdAt BETWEEN :start AND :end")
    long countCardsBeetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
