package com.autumnsoftwares.agendamento.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@EnableWebSecurity
@SecurityScheme(name = SecurityConfigurations.SECURITY, type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SecurityConfigurations {

    public static final String SECURITY = "bearerAuth";

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, SecurityFilter securityFilter) throws Exception {
        return httpSecurity
                .csrf(crsf -> crsf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        // .requestMatchers(HttpMethod.POST, "auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/barber").permitAll()
                        .requestMatchers("/barber/**").hasRole("ADMIN")
                        .requestMatchers("/customer/**").permitAll()
                        .requestMatchers("/scheduling/**").permitAll()
                        // .requestMatchers("/barber-shop/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/barber-shop").permitAll()
                        .requestMatchers(HttpMethod.POST, "/services/**").hasRole("ADMIN")
                        .requestMatchers("/v3/api-docs/**", "swagger-ui/**", "swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
