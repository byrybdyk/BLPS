package com.zarubovandlevchenko.lb1.repo;

import com.zarubovandlevchenko.lb1.model.UserModal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModal, Long> {

    UserModal findByLoginOrPhoneNumber(String login, String login1);

    boolean existsByPassportNumber(String passportNumber);
}
