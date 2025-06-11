package com.zarubovandlevchenko.lb1.security;

import com.zarubovandlevchenko.lb1.config.XmlLoginModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.*;
import org.springframework.util.StringUtils;

import javax.security.auth.login.AppConfigurationEntry;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
//                )
                .csrf(csrf -> csrf.disable())

                .httpBasic(httpBasic -> httpBasic.realmName("XZApp"))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/registration-requests").hasRole("SB")
                        .requestMatchers("/api/v1/cards").hasAnyRole("SB", "BE")
                        .requestMatchers("/api/v1/cards/{}/issue").hasRole("BE")
                        .requestMatchers("/api/v1/auth/", "api/v1/registration/**").permitAll()
                        .requestMatchers("/api/v1/registration-requests/webhook/**").permitAll()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

//    final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
//        private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
//        private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();
//
//        @Override
//        public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
//            this.xor.handle(request, response, csrfToken);
//            csrfToken.get();
//        }
//        @Override
//        public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
//            String headerValue = request.getHeader(csrfToken.getHeaderName());
//            return (StringUtils.hasText(headerValue) ? this.plain : this.xor).resolveCsrfTokenValue(request, csrfToken);
//        }}



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

        provider.setLoginContextName("XZApp");
        provider.setConfiguration(new javax.security.auth.login.Configuration() {
            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
                if ("XZApp".equals(name)) {
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