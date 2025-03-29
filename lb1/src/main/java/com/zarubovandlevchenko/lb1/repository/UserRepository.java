package com.zarubovandlevchenko.lb1.repository;

import com.zarubovandlevchenko.lb1.model.UserModal;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserModal, Long> {

    UserModal findByLoginOrPhoneNumber(String login, String login1);

    boolean existsByPassportNumber(String passportNumber);

    UserModal  findUserById(Long userId);

    UserModal findUserModalById(Long id);
}
