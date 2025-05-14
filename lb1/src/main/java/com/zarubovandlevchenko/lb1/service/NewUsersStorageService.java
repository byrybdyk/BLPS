package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NewUsersStorageService {
    private final Map<UserModal, String> newUsersStorage = new ConcurrentHashMap<>();
    private final Map<Long, UserModal> UsersRegistrationRequests = new ConcurrentHashMap<>();
    private final Map<String, String> jiraIssueKeys = new ConcurrentHashMap<>(); // phoneNumber -> issueKey

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

    public Map<Long, UserModal> getRegistrationRequests() {
        return UsersRegistrationRequests;
    }

    public void addUsersRegistrationRequest(UserModal user) {
        Long requestsId = (long) user.hashCode();
        UsersRegistrationRequests.put(requestsId, user);
    }

    public void removeUsersRegistrationRequestById(Long id) {
        UsersRegistrationRequests.remove(id);
    }

    public void restoreUserRegistrationRequest(UserModal user) {
        Long requestId = (long) user.hashCode();
        UsersRegistrationRequests.put(requestId, user);
    }

    public void addJiraIssueKey(UserModal user, String issueKey) {
        jiraIssueKeys.put(user.getPhoneNumber(), issueKey);
    }

    public String getJiraIssueKey(String phoneNumber) {
        return jiraIssueKeys.get(phoneNumber);
    }

    public void removeJiraIssueKey(String phoneNumber) {
        jiraIssueKeys.remove(phoneNumber);
    }
}
