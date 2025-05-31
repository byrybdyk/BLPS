package com.zarubovandlevchenko.lb1.jira;

import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;

public interface JiraConnection {

    void createIssue(UserModal user, Long requestId) throws Exception;
}