package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.dto.SignUpRequest;
import com.zarubovandlevchenko.lb1.exception.UserNotFoundException;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import com.zarubovandlevchenko.lb1.repository.dbuser.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserModal validateAndCreateUser(SignUpRequest request) {
        if(validateUserFirstOrSecondName(request.getName())&&
                validateUserFirstOrSecondName(request.getLastName())&&
            validatePhoneNumber(request.getPhoneNumber())&&
                validatePassport(request.getPassport())
        ){
            UserModal newUser = new UserModal();
            newUser.setFirstName(request.getName());
            newUser.setLastName(request.getLastName());
            newUser.setPhoneNumber(request.getPhoneNumber());
            newUser.setPassportNumber(request.getPassport());
            return newUser;
        }
        return null;
    }

    private boolean validatePassport(String passport) {
        if (passport == null || passport.isEmpty()) {
            throw new IllegalArgumentException("Passport number cannot be empty");
        }
        if (!passport.matches("\\d{4} \\d{6}")) {
            throw new IllegalArgumentException("Passport number must be in the format XXXX XXXXXX");
        }
        if (userRepository.existsByPassportNumber(passport)) {
            throw new IllegalArgumentException("Passport already exists");
        }
        return true;
    }


    private boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        if (!phoneNumber.matches("\\+7\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be in the format +7XXXXXXXXXX");
        }
        return true;
    }

    private boolean validateUserFirstOrSecondName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return true;
    }

    public UserModal saveUser(UserModal userModal) {
        if(userModal == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(userModal);
    }

    public List<UserModal> getUsers() {
        List<UserModal> users = userRepository.findAll();
        if (users.isEmpty()) {
            return null;
        }
        return users;
    }

    public UserModal getUserById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
