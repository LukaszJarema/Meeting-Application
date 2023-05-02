package com.jarema.lukasz.Meeting.Application.security;

import com.jarema.lukasz.Meeting.Application.services.impls.CustomAdministratorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1)
public class AdministratorSecurityConfig {

    private CustomAdministratorDetailsService customAdministratorDetailsService;

    @Autowired
    public AdministratorSecurityConfig(CustomAdministratorDetailsService customAdministratorDetailsService) {
        this.customAdministratorDetailsService = customAdministratorDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeHttpRequests().requestMatchers("/", "/login**", "/register", "/employees/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMINISTRATOR")
                .and()
                .formLogin().permitAll()
                .and()
                .logout()
                .permitAll();

        return http.build();
    }

    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(customAdministratorDetailsService).passwordEncoder(passwordEncoder());
    }
}
