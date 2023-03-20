package com.dangol.dangolsonnimbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("static/docs/**").permitAll()
                .antMatchers("/templates/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .csrf()
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("**/post", "POST"))
                        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("**/put", "PUT"))
                        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("**/patch", "PATCH"));
        return http.build();
    }
}
