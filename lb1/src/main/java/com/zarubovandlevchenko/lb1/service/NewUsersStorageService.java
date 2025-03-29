package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.model.UserModal;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NewUsersStorageService {
    private final Map<UserModal, String> newUsersStorage = new ConcurrentHashMap<>();

    public void addNewUser(UserModal user, String phoneNumber) {
        newUsersStorage.put(user, phoneNumber);
    }

    public UserModal getNewUser(String phoneNumber) {
        for (Map.Entry<UserModal, String> entry : newUsersStorage.entrySet()) {
            if (entry.getValue().equals(phoneNumber)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void removeNewUser(UserModal user) {
        newUsersStorage.remove(user);
    }
}
