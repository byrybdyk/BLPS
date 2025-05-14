package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.service.JiraService;
import com.zarubovandlevchenko.lb1.service.NewUsersStorageService;
import com.zarubovandlevchenko.lb1.service.SecurityAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/webhook/jira")
@RequiredArgsConstructor
public class JiraWebhookController {

    private final JiraService jiraService;
    private final NewUsersStorageService newUsersStorageService;
    private final SecurityAdminService securityAdminService;

    @PostMapping
    public ResponseEntity<?> handleJiraWebhook(@RequestBody Map<String, Object> webhook) {
        try {
            Map<String, Object> issue = (Map<String, Object>) webhook.get("issue");
            String issueKey = (String) issue.get("key");
            Map<String, Object> changelog = (Map<String, Object>) webhook.get("changelog");
            
            // Find user by Jira issue key
            String phoneNumber = findUserByJiraIssue(issueKey);
            if (phoneNumber == null) {
                log.warn("No user found for Jira issue {}", issueKey);
                return ResponseEntity.ok().build();
            }

            // Get the new status
            String newStatus = getNewStatus(changelog);
            if (newStatus == null) {
                return ResponseEntity.ok().build();
            }

            // Handle the status change
            switch (newStatus) {
                case "Approved":
                    securityAdminService.approveRequestByPhone(phoneNumber);
                    break;
                case "Rejected":
                    securityAdminService.rejectRequestByPhone(phoneNumber);
                    break;
                default:
                    log.info("Unhandled status change to {} for issue {}", newStatus, issueKey);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error processing Jira webhook", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private String findUserByJiraIssue(String issueKey) {
        for (Map.Entry<String, String> entry : newUsersStorageService.getJiraIssueKeys().entrySet()) {
            if (entry.getValue().equals(issueKey)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String getNewStatus(Map<String, Object> changelog) {
        if (changelog == null) {
            return null;
        }

        Map<String, Object> items = (Map<String, Object>) changelog.get("items");
        if (items == null) {
            return null;
        }

        for (Map<String, Object> item : (Iterable<Map<String, Object>>) items) {
            if ("status".equals(item.get("field"))) {
                return (String) item.get("toString");
            }
        }
        return null;
    }
} 