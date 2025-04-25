package com.zarubovandlevchenko.lb1.repository.dbuser;

import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserModal, Long> {

    UserModal findByLoginOrPhoneNumber(String login, String login1);

    boolean existsByPassportNumber(String passportNumber);

    UserModal  findUserById(Long userId);

    UserModal findUserModalById(Long id);

    Optional<UserModal> findUserModalByPassportNumber(String passportNumber);
}
