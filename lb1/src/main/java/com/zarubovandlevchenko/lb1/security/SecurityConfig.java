package com.zarubovandlevchenko.lb1.security;

import com.zarubovandlevchenko.lb1.config.XmlLoginModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import javax.security.auth.login.AppConfigurationEntry;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable())

                .httpBasic(httpBasic -> httpBasic.realmName("GrokApp"))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/registration-requests").hasRole("SB")
                        .requestMatchers("/api/v1/cards").hasAnyRole("SB", "BE")
                        .requestMatchers("/api/v1/cards/{}/issue").hasRole("BE")
                        .requestMatchers("/api/v1/auth/").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public DefaultJaasAuthenticationProvider jaasAuthenticationProvider() {
        DefaultJaasAuthenticationProvider provider = new DefaultJaasAuthenticationProvider();

        Map<String, String> options = new HashMap<>();
        options.put("usersFile", "src/main/resources/users.xml");

        AppConfigurationEntry entry = new AppConfigurationEntry(
                "com.zarubovandlevchenko.lb1.config.XmlLoginModule",
                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                options
        );

        provider.setLoginContextName("GrokApp");
        provider.setConfiguration(new javax.security.auth.login.Configuration() {
            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
                if ("GrokApp".equals(name)) {
                    return new AppConfigurationEntry[]{entry};
                }
                return null;
            }
        });

        provider.setAuthorityGranters(new AuthorityGranter[] { new AuthorityGranter() {
            @Override
            public Set<String> grant(Principal principal) {
                Set<String> roles = new HashSet<>();
                if (principal instanceof XmlLoginModule.RolePrincipal) {
                    String role = principal.getName();
                    if (!role.startsWith("ROLE_")) {
                        roles.add("ROLE_" + role);
                        System.out.println("Granting role for principal: ROLE_" + role);
                    } else {
                        roles.add(role);
                        System.out.println("Granting role for principal: " + role);
                    }
                }
                System.out.println("Roles assigned: " + roles);
                return roles;
            }
        }});

        return provider;
    }
}