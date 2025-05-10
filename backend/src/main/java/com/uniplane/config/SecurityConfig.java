package com.uniplane.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.uniplane.config.JwtAuthFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                //.requestMatchers("/api/stack/**").hasRole("ADMIN")
                .requestMatchers("/api/stack/**").permitAll()
                .requestMatchers("/api/user/**").authenticated()
                .requestMatchers("/api/cloud-resource/**").authenticated()
                .requestMatchers("/api/stack-deployment/**").authenticated()    
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
