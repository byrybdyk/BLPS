package com.zarubovandlevchenko.lb1.jira;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class JiraConnectionImpl implements JiraConnection {

    @Value("${jira.url}")    private String jiraUrl;
    @Value("${jira.email}")  private String jiraEmail;
    @Value("${jira.token}")  private String jiraToken;
    @Value("${jira.projectKey}") private String projectKey;

    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void createIssue(UserModal user, Long requestId) throws Exception {

        /* ---------- строим JSON ---------- */
        ObjectNode root   = om.createObjectNode();
        ObjectNode fields = root.putObject("fields");

        fields.putObject("project").put("key", projectKey);
        fields.put("summary", requestId.toString());

        // description — Atlassian Document Format
        ObjectNode desc = fields.putObject("description");
        desc.put("type", "doc").put("version", 1);
        ArrayNode descContent = desc.putArray("content");
        ObjectNode paragraph  = descContent.addObject();
        paragraph.put("type", "paragraph");
        ArrayNode  pContent   = paragraph.putArray("content");
        pContent.addObject()
                .put("type", "text")
                .put("text", "Новая заявка на регистрацию от пользователя: "+
                        user.getLastName()+ " "+  user.getFirstName()+ ", телефон "  + user.getPhoneNumber());

        fields.putObject("issuetype").put("id", "10003");   // Submit a request or incident

        String payload = om.writeValueAsString(root);
        /* ---------- /строим JSON ---------- */

        String auth = Base64.getEncoder()
                .encodeToString((jiraEmail + ":" + jiraToken)
                        .getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(jiraUrl + "/rest/api/3/issue"))
                .header("Authorization", "Basic " + auth)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> resp = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() >= 300) {
            throw new RuntimeException("Ошибка JIRA: "
                    + resp.statusCode() + " " + resp.body());
        }
    }
}
