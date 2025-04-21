package com.zarubovandlevchenko.lb1.config;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.Subject;
import java.io.File;
import java.security.Principal;
import java.util.*;
//LoginModule implementation of the Java Authentication and Authorization Service (JAAS).
// It is used to authenticate users based on data from the users.xml file.
public class XmlLoginModule implements javax.security.auth.spi.LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, ?> sharedState;
    private Map<String, ?> options;
    private boolean loginSucceeded = false;
    private Principal userPrincipal;
    private List<String> roles = new ArrayList<>();
    private Integer userId;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
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
                if (username.startsWith("card:") && validateCard(user, username.substring(5), password)) {
                    loginSucceeded = true;
                    userPrincipal = new UserPrincipal(username);
                    roles = user.getRoles();
                    userId = user.getId();
                    return true;
                } else if (username.startsWith("login:") && validateLogin(user, username.substring(6), password)) {
                    loginSucceeded = true;
                    userPrincipal = new UserPrincipal(username);
                    roles = user.getRoles();
                    userId = user.getId();
                    return true;
                } else if (username.startsWith("phone:") && validatePhone(user, username.substring(6), password)) {
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

    private boolean validateCard(User user, String cardNumber, String otp) {
        return user.getCredentials().getCard() != null &&
                user.getCredentials().getCard().getNumber().equals(cardNumber) &&
                user.getCredentials().getCard().getOtp().equals(otp);
    }

    private boolean validateLogin(User user, String username, String password) {
        return user.getCredentials().getLogin() != null &&
                user.getCredentials().getLogin().getUsername().equals(username) &&
                user.getCredentials().getLogin().getPassword().equals(password);
    }

    private boolean validatePhone(User user, String phoneNumber, String password) {
        return user.getCredentials().getPhone() != null &&
                user.getCredentials().getPhone().getNumber().equals(phoneNumber) &&
                user.getCredentials().getPhone().getPassword().equals(password);
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

    public static class Users {
        private List<User> users;

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    public static class User {
        private Integer id;
        private List<String> roles;
        private Credentials credentials;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }
    }

    public static class Credentials {
        private Card card;
        private Login login;
        private Phone phone;

        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
        }

        public Login getLogin() {
            return login;
        }

        public void setLogin(Login login) {
            this.login = login;
        }

        public Phone getPhone() {
            return phone;
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }
    }

    public static class Card {
        private String number;
        private String otp;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }

    public static class Login {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class Phone {
        private String number;
        private String password;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
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
