package com.zarubovandlevchenko.lb1.config;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.Subject;
import java.io.File;
import java.security.Principal;
import java.util.*;

public class XmlLoginModule implements javax.security.auth.spi.LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private boolean loginSucceeded = false;
    private Principal userPrincipal;
    private List<String> roles = new ArrayList<>();
    private Integer userId;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public boolean login() throws LoginException {
        try {
            NameCallback nameCallback = new NameCallback("Username: ");
            PasswordCallback passwordCallback = new PasswordCallback("Password: ", false);
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});

            String username = nameCallback.getName();
            String password = new String(passwordCallback.getPassword());

            XmlMapper xmlMapper = new XmlMapper();
            File xmlFile = new File("src/main/resources/users.xml");
            Users users = xmlMapper.readValue(xmlFile, Users.class);

            for (User user : users.getUsers()) {
                    if (validateLogin(user, username, password)) {
                        loginSucceeded = true;
                        userPrincipal = new UserPrincipal(username);
                        roles = user.getRoles();
                        userId = user.getId();
                        return true;
                    }
            }
            throw new FailedLoginException("Invalid credentials");
        } catch (Exception e) {
            throw new LoginException(e.getMessage());
        }
    }


    private boolean validateLogin(User user, String username, String password) {
        return user.getCredentials().getLogin() != null &&
                user.getCredentials().getLogin().getUsername().equals(username) &&
                user.getCredentials().getLogin().getPassword().equals(password);
    }


    @Override
    public boolean commit() throws LoginException {
        if (!loginSucceeded) {
            return false;
        }
        subject.getPrincipals().add(userPrincipal);
        for (String role : roles) {
            subject.getPrincipals().add(new RolePrincipal(role));
        }
        subject.getPrincipals().add(new IdPrincipal(userId));
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        logout();
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().clear();
        loginSucceeded = false;
        userPrincipal = null;
        roles.clear();
        userId = null;
        return true;
    }

    @Setter
    @Getter
    public static class Users {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "user")
        private List<User> users;

    }

    @Setter
    @Getter
    public static class User {
        private Integer id;
        private List<String> roles;
        private Credentials credentials;

    }

    @Setter
    @Getter
    public static class Credentials {
        private Card card;
        private Login login;
        private Phone phone;

    }

    @Setter
    @Getter
    public static class Card {
        private String number;
        private String otp;

    }

    @Setter
    @Getter
    public static class Login {
        private String username;
        private String password;

    }

    @Setter
    @Getter
    public static class Phone {
        private String number;
        private String password;

    }

    private static class UserPrincipal implements Principal {
        private final String name;

        public UserPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }


    public static class RolePrincipal implements Principal {
        private String name;

        public RolePrincipal(String name) {
            this.name = name;
        }
        public RolePrincipal() {
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean implies(Subject subject) {
            return Principal.super.implies(subject);
        }
    }

    private static class IdPrincipal implements Principal {
        private final Integer id;

        public IdPrincipal(Integer id) {
            this.id = id;
        }

        @Override
        public String getName() {
            return id.toString();
        }
    }
}
