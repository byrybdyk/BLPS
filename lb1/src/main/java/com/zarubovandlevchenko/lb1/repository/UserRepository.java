package com.zarubovandlevchenko.lb1.repository;

import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserModal, Long> {

    UserModal findByLoginOrPhoneNumber(String login, String login1);

    boolean existsByPassportNumber(String passportNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserModal> findUserModalById(Long id);

    Optional<UserModal> findUserModalByPassportNumber(String passportNumber);

    @Query("SELECT count(c) from UserModal c WHERE c.createdAt BETWEEN :start AND :end")
    long countUsersBeetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
