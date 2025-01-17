package com.quickmed.auth.config;

import com.quickmed.auth.providers.OTPAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static String[] PUBLIC_PATHS = new String[]{"/api/auth/public/**"};

    /**
     * @Bean annotation is used in Spring to indicate that the method will return a bean
     * that should be managed by the Spring container.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Disable CSRF since tokens are immune to it
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(PUBLIC_PATHS).permitAll() // These paths are allowed for everyone without authentication.
                                .anyRequest().authenticated() // Any other request must be authenticated.
                )
                // Set session management to stateless
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Disable form login and
                .formLogin(AbstractHttpConfigurer::disable)
                // HTTP Basic Authentication - Will only enable REST access.
                // Imagine using "/main" through REST Client like Postman with Authorization -> Basic Auth -> username / password
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /**
     * @return
     * The method returns an instance of AuthenticationProvider,
     * which is a core interface in Spring Security responsible for processing authentication requests.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new OTPAuthenticationProvider();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
