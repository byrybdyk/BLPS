package com.zarubovandlevchenko.lb1.service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class JiraService {

    private final JiraRestClient jiraRestClient;

    @Value("${jira.project-key}")
    private String projectKey;

    @Value("${jira.issue-type}")
    private String issueType;

    public String createRegistrationIssue(UserModal user) {
        try {
            IssueInputBuilder issueBuilder = new IssueInputBuilder()
                    .setProjectKey(projectKey)
                    .setIssueType(issueType)
                    .setSummary("New User Registration Request")
                    .setDescription(String.format(
                            "New user registration request:\n" +
                            "Name: %s %s\n" +
                            "Phone: %s\n" +
                            "Passport: %s",
                            user.getFirstName(),
                            user.getLastName(),
                            user.getPhoneNumber(),
                            user.getPassportNumber()
                    ));

            IssueInput issueInput = issueBuilder.build();
            Issue issue = jiraRestClient.getIssueClient()
                    .createIssue(issueInput)
                    .get();

            log.info("Created Jira issue {} for user registration", issue.getKey());
            return issue.getKey();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to create Jira issue", e);
            throw new RuntimeException("Failed to create Jira issue", e);
        }
    }

    public void updateIssueStatus(String issueKey, String status) {
        try {
            jiraRestClient.getIssueClient()
                    .transition(jiraRestClient.getIssueClient()
                            .getIssue(issueKey)
                            .get(), status)
                    .get();
            log.info("Updated Jira issue {} status to {}", issueKey, status);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to update Jira issue status", e);
            throw new RuntimeException("Failed to update Jira issue status", e);
        }
    }
} 